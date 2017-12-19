package com.svlada.endpoint.font;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.request.CustomResponseStatus;
import com.svlada.common.utils.CommonUtil;
import com.svlada.common.utils.OrderShipUtil;
import com.svlada.common.utils.OrderUtil;
import com.svlada.common.utils.wx.PayUtil;
import com.svlada.common.utils.wx.TimeUtil;
import com.svlada.component.repository.*;
import com.svlada.component.service.OrderServiceImpl;
import com.svlada.component.service.TradeService;
import com.svlada.endpoint.dto.OrderDto;
import com.svlada.endpoint.dto.OrderInfoDto;
import com.svlada.endpoint.dto.OrderItemDto;
import com.svlada.endpoint.dto.builder.OrderInfoBuilder;
import com.svlada.entity.*;
import com.svlada.entity.product.Product;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;
import static com.svlada.entity.product.Product.STATUS_DOWN;

@RestController
@RequestMapping("/api/font/order")
public class FontOrderEndpoint {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private OrderShipRepository orderShipRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ShopCartRepository shopCartRepository;

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartnerRepository partnerRepository;

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @ApiOperation(value = "创建订单", notes = "配送记录，订单详情，微信预支付订单号,商户订单号等")
    @ApiImplicitParam()
    @PostMapping(value = "/create")
    public CustomResponse create(@RequestBody OrderDto orderDto, HttpServletRequest request) {
        LOGGER.info("创建订单");
        if (StringUtils.isEmpty(orderDto.getAddressId())){
            return fail(CustomResponseStatus._40104,"请添加收货地址");
        }
        User user = WebUtil.getCurrentUser();
        Long userId = user.getId();
        //配送地址记录生成
        Address address = addressRepository.findOne(orderDto.getAddressId());
        if (address == null || !userId.equals(address.getUser().getId())) {
            return fail(CustomResponseStatus._40104,"请添加收货地址");
        }
        if (orderDto.isFromCart()) {
            List<Long> productIds = new ArrayList<>();
            Long[] array = new Long[productIds.size()];
            OrderItemDto[] orderItemDtos = orderDto.getOrderItemDtos();
            for (OrderItemDto orderItemDto : orderItemDtos) {
                Long productId = orderItemDto.getProductId();
                productIds.add(productId);
            }
            //删除购物车中被购买的商品
            array = productIds.toArray(array);
            shopCartRepository.deleteAllByUserIdAndProductIdIn(userId, array);
            LOGGER.info("删除已下单的购物车中的商品！");
        }

        //生成业务订单记录
        Order order = new Order();
        order.setUser(WebUtil.getCurrentUser());
        order.setCreateDate(new Date());
        Partner shareToMe = partnerRepository.findFirstByUserId(userId);//分享给我的人
        if (shareToMe!=null){
            User shareUser = shareToMe.getShareUser();
            if (shareUser!=null&& shareUser.getMember()){//分享人是会员[购买过商品]
                LOGGER.info("该订单为分享后的订单，分享人有提成！");
                order.setShareId(shareUser.getOpenId());
                order.setShareFlag(false);//分享的订单为结算，需要支付成功后再后台结算
            }else {
                LOGGER.error("分享人用户信息出错！不存在！");
            }
        }else {
            LOGGER.info("该用户不是通过会员分享登陆的，所以没有回扣计算！");
        }

        if (!StringUtils.isEmpty(orderDto.getShareId())) {
            User oneByOpenId = userRepository.findOneByOpenId(orderDto.getShareId());
            if (oneByOpenId != null) {
                order.setShareId(oneByOpenId.getOpenId());
            }
        }

        Set<Product> products = new HashSet<>();
        String goodsName = "";

        Set<OrderItem> orderItems = new HashSet<>();
        Long totalMoney = 0L;
        for (OrderItemDto orderItemDto : orderDto.getOrderItemDtos()) {
            Long productId = orderItemDto.getProductId();
            Product product = productRepository.findOne(productId);
            if (product != null) {
                if (STATUS_DOWN.equals(product.getStatus())) {
                    LOGGER.error("参数不正确！STATUS_DOWN");
                    return fail(CustomResponseStatus._40105, "商品已下架");
                }
                if (product.getStock() < orderItemDto.getNumber()) {
                    LOGGER.error("参数不正确！Number");
                    return fail(CustomResponseStatus._40106, "商品库存不足!");
                }
                products.add(product);
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setNumber(orderItemDto.getNumber());
                orderItem.setUnitPrice(product.getNowPrice());
                Long itemTotalMoney = orderItem.getNumber() * product.getNowPrice();
                orderItem.setItemTotalMoney(itemTotalMoney);
                totalMoney +=itemTotalMoney;
                orderItem.setName(product.getName());
                orderItems.add(orderItem);
                goodsName = goodsName + product.getName() + " ";
            }
        }
        String body = goodsName;
        String details = "用户购买的商品有:" + goodsName;
        //订单记录对象
        String outTradeNo = OrderUtil.genOrderCode();
        order.setOutTradeNo(outTradeNo);
        order.setItems(orderItems);
        order.setTotalMoney(totalMoney);
        order.setBody(body);
        order.setDetails(details);
        order.setPayStatus(Order.pay_status_init);

        OrderShip orderShip = OrderShipUtil.getOrderShip(orderDto, address);
        //存储更新操作
        orderService.save(products, orderItems, order, orderShip);

        String ip = CommonUtil.getIpAddr(request);
        //生成微信预支付订单的编号：prepayId
        User currentUser = WebUtil.getCurrentUser();
        if (currentUser==null || StringUtils.isEmpty(currentUser.getOpenId())){
            LOGGER.error("未登录，无法下单！");
            return fail();
        }
        Map<String, String> mapPay = PayUtil.weixinPrePay(order.getOutTradeNo(), order.getTotalMoney() + "", details, currentUser.getOpenId(), ip);
        SortedMap<String, Object> finalpackage = new TreeMap<String, Object>();
        finalpackage.put("appId", PayUtil.APPID);
        finalpackage.put("timeStamp", TimeUtil.getTimeStamp());
        finalpackage.put("nonceStr", PayUtil.getRandomString(32));
        finalpackage.put("package", "prepay_id=" + mapPay.get("prepay_id"));
        finalpackage.put("signType", "MD5");
        String sign = PayUtil.createSign("UTF-8", finalpackage);
        finalpackage.put("paySign", sign);
        finalpackage.put("prepay_id", mapPay.get("prepay_id"));
        finalpackage.remove("appId");
        return success(finalpackage);
    }


    @GetMapping("/get/status/{outTradeNum}")
    public CustomResponse status(@PathVariable("outTradeNum") String outTradeNum) {
        Order order = orderRepository.findOneByOutTradeNo(outTradeNum);
        if (order == null) {
            return fail(CustomResponseStatus._40401, "记录不存在!");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        /*
        map.put("paymentDate", order.getPaymentDate());
        map.put("outTradeNo", order.getWxpayNotify().getOutTradeNo());
        */
        map.put("payStatus", order.getPayStatus());
        return success(order.getPayStatus());
    }

    @GetMapping("/list")
    public CustomResponse list(@PageableDefault(value = 20, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        Page<OrderInfoDto> dtoPage = orderPage.map(entity -> {
            OrderInfoDto dto = OrderInfoBuilder.builderOrderInfoDto(entity);
            return dto;
        });
        return success(dtoPage);
    }

    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id) {
        Order order = orderRepository.findOne(id);
        return success(order);
    }

    @PutMapping(value = "/update")
    public CustomResponse update(@RequestBody OrderDto dto) {
        Order order = orderRepository.findOne(dto.getId());
        orderRepository.save(order);
        return success();
    }

    @PutMapping(value = "/delete")
    public CustomResponse delete(@RequestBody OrderDto dto) {
        Order order = orderRepository.findOne(dto.getId());
        orderRepository.save(order);
        return success();
    }

}

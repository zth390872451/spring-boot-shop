package com.svlada.endpoint.backstage;

import com.svlada.common.request.CustomResponse;
import com.svlada.common.utils.DateUtils;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.OrderShipRepository;
import com.svlada.component.repository.UserRepository;
import com.svlada.endpoint.dto.*;
import com.svlada.entity.Order;
import com.svlada.entity.OrderShip;
import com.svlada.entity.User;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/back/order")
public class BackOrderEndpoint {
    private static final Logger log = LoggerFactory.getLogger(BackOrderEndpoint.class);
    @Autowired
    private OrderRepository orderRepository;

    @ApiOperation(value = "订单信息列表", notes = "订单信息列表分页")
    @ApiImplicitParams({
    })
    @GetMapping(value = "/list")
    public CustomResponse list(@RequestParam(name = "wechatCode", required = false) String wechatCode,
                               @RequestParam(name = "orderCode", required = false) String orderCode,
                               @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = DateUtils.FULL_DATE_FORMAT) Date startDate,
                               @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = DateUtils.FULL_DATE_FORMAT) Date endDate,
                               @RequestParam(name = "payStatus", required = false) Integer payStatus,
                               @PageableDefault(value = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                       Pageable pageable) {
        OrderSearchDto dto = new OrderSearchDto(wechatCode, orderCode, startDate, endDate, payStatus);
        PageRequest pageRequest = null;
        pageRequest = new PageRequest(0, 10, Sort.Direction.DESC, "id");
        Page<Order> orderPage = orderRepository.findAll(getSpecification(dto), pageable);
        Page<OrderInfoDto> dtoPage = orderPage.map(entity -> {
            OrderInfoDto orderInfoDto = new OrderInfoDto();
            orderInfoDto.setBody(entity.getBody());
            orderInfoDto.setDetails(entity.getDetails());
            orderInfoDto.setId(entity.getId());
            orderInfoDto.setOutTradeNo(entity.getOutTradeNo());
            orderInfoDto.setPaymentDate(DateUtils.getFormatDate(entity.getPaymentDate(), DateUtils.FULL_DATE_FORMAT));
            orderInfoDto.setPayStatus(entity.getPayStatus());
            orderInfoDto.setTotalMoney(entity.getTotalMoney());
            orderInfoDto.setShareFlag(entity.getShareFlag());//是否已结算
            if (entity.getWxpayNotify() != null) {
                orderInfoDto.setTransactionId(entity.getWxpayNotify().getTransactionId());
            }
            User user = entity.getUser();
            if (user!=null){
                orderInfoDto.setNickName(user.getNickName());
                orderInfoDto.setOpenId(user.getOpenId());
            }
            if (!StringUtils.isEmpty(entity.getShareId())){//有分享人则有佣金显示
                User shareUser = userRepository.findOneByOpenId(entity.getShareId());
                if (shareUser!=null){
                    orderInfoDto.setShareNickName(shareUser.getNickName());
                }
                orderInfoDto.setShareFlag(entity.getShareFlag());
            }

            return orderInfoDto;
        });
        return success(dtoPage);
    }



    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderShipRepository orderShipRepository;

    @ApiOperation(value = "订单信息列表导出列表", notes = "订单信息列表导出列表")
    @ApiImplicitParams({
    })
    @GetMapping(value = "/exportList")
    public CustomResponse exportList(@RequestParam(name = "wechatCode", required = false) String wechatCode,
                       @RequestParam(name = "orderCodes", required = false) String orderCodes,
                       @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = DateUtils.FULL_DATE_FORMAT) Date startDate,
                       @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = DateUtils.FULL_DATE_FORMAT) Date endDate,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        OrderExportDto dto = new OrderExportDto(wechatCode,orderCodes,startDate, endDate, Order.pay_status_success);
        List<Order> orderList = orderRepository.findAll(getSpecification(dto));
        log.info("商品正在导出列表!");
        List<OrderExportInfoDto> orderInfoDtos = orderList.stream().map(entity -> {
            OrderExportInfoDto orderInfoDto = new OrderExportInfoDto();
            //订单详情信息
            orderInfoDto.setBody(entity.getBody());
            orderInfoDto.setDetails(entity.getDetails());
            orderInfoDto.setId(entity.getId());
            orderInfoDto.setOutTradeNo(entity.getOutTradeNo());
            orderInfoDto.setPaymentDate(DateUtils.getFormatDate(entity.getPaymentDate(), DateUtils.FULL_DATE_FORMAT));
            orderInfoDto.setPayStatus(entity.getPayStatus());
            orderInfoDto.setTotalMoney(entity.getTotalMoney());
            //配送信息
            OrderShip orderShip = orderShipRepository.findOneByOrderId(entity.getId());
//            OrderShip orderShip = entity.getOrderShip();
            orderInfoDto.setArea(orderShip.getArea());
            orderInfoDto.setCity(orderShip.getCity());
            orderInfoDto.setConsigneeAddress(orderShip.getConsigneeAddress());
            orderInfoDto.setPhone(orderShip.getPhone());
            orderInfoDto.setProvince(orderShip.getProvince());
            orderInfoDto.setRemark(orderShip.getRemark());
            orderInfoDto.setSex(orderShip.getSex());
            orderInfoDto.setTel(orderShip.getTel());
            orderInfoDto.setConsigneeName(orderShip.getConsigneeName());
            orderInfoDto.setShareFlag(entity.getShareFlag());
            if (entity.getWxpayNotify() != null) {
                orderInfoDto.setTransactionId(entity.getWxpayNotify().getTransactionId());
            }
            if (!StringUtils.isEmpty(entity.getShareId())){
                User oneByOpenId = userRepository.findOneByOpenId(entity.getShareId());
                if (oneByOpenId!=null){
                    orderInfoDto.setShareUserName(oneByOpenId.getNickName());
                    orderInfoDto.setShareUserOpenId(oneByOpenId.getOpenId());
                }
            }
            return orderInfoDto;
        }).collect(Collectors.toList());
        return success(orderInfoDtos);
    }

    @GetMapping(value = "/update")
    public CustomResponse update(@RequestParam(name = "orderCodes", required = true) String orderCodes) {
        String[] orderIds = orderCodes.split(",");
        List<String> ids = Arrays.asList(orderIds);
        Boolean shareFlag =true;//结算
        orderRepository.updateShareFlag(ids,shareFlag);
        return success();
    }
    /**
     * in
     *
     * @param root
     * @param propertyName
     *            属性名称
     * @param value
     */
    public void createIn(List<Predicate> predicates, Root<Order> root, CriteriaBuilder criteriaBuilder, String propertyName, Collection value) {
        if ((value == null) || (value.size() == 0)) {
            return;
        }
        Iterator iterator = value.iterator();
        CriteriaBuilder.In in = criteriaBuilder.in(root.get(propertyName));
        while (iterator.hasNext()) {
            in.value(iterator.next());
        }
        predicates.add(in);
    }

    private Specification<Order> getSpecification(OrderExportDto dto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(dto.getPayStatus())) {
                Predicate predicate = cb.equal(root.get("payStatus").as(Integer.class),dto.getPayStatus());
                predicates.add(predicate);
            }
            if (!StringUtils.isEmpty(dto.getOrderCodes())) {
                String orderCodes = dto.getOrderCodes();
                String[] split = orderCodes.split(",");
                List<String> collection = Arrays.asList(split);
                this.createIn(predicates,root,cb,"outTradeNo",collection);
            }
            if (!StringUtils.isEmpty(dto.getWechatCode())) {
                Predicate predicate = cb.equal(root.get("wechatCode").as(String.class), dto.getWechatCode());
                predicates.add(predicate);
            }
            if (!StringUtils.isEmpty(dto.getStartDate()) && !StringUtils.isEmpty(dto.getEndDate())) {
                Predicate predicate = cb.between(root.<Date>get("paymentDate"), dto.getStartDate(), dto.getEndDate());
                predicates.add(predicate);
            }
            Predicate[] pre = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pre)).getRestriction();
        };
    }

    private Specification<Order> getSpecification(OrderSearchDto orderSearchDto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(orderSearchDto.getPayStatus())) {
                Predicate predicate = cb.equal(root.get("payStatus").as(Integer.class), orderSearchDto.getPayStatus());
                predicates.add(predicate);
            }
            if (!StringUtils.isEmpty(orderSearchDto.getOrderCode())) {
                Predicate predicate = cb.equal(root.get("outTradeNo").as(String.class), orderSearchDto.getOrderCode());
                predicates.add(predicate);
            }
            if (!StringUtils.isEmpty(orderSearchDto.getWechatCode())) {
                Predicate predicate = cb.equal(root.get("wechatCode").as(String.class), orderSearchDto.getWechatCode());
                predicates.add(predicate);
            }
            if (!StringUtils.isEmpty(orderSearchDto.getStartDate()) && !StringUtils.isEmpty(orderSearchDto.getStartDate())) {
                Predicate predicate = cb.between(root.<Date>get("paymentDate"), orderSearchDto.getStartDate(), orderSearchDto.getEndDate());
                predicates.add(predicate);
            }
            Predicate[] pre = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pre)).getRestriction();
        };
    }




    @ApiOperation(value = "订单信息列表导出", notes = "订单信息列表导出")
    @ApiImplicitParams({
    })
    @GetMapping(value = "/export")
    public void export(@RequestParam(name = "wechatCode", required = false) String wechatCode,
                       @RequestParam(name = "orderCode", required = false) String orderCode,
                       @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = DateUtils.FULL_DATE_FORMAT) Date startDate,
                       @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = DateUtils.FULL_DATE_FORMAT) Date endDate,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=OrderSuccess.xls");
        OrderExportDto dto = new OrderExportDto(wechatCode,orderCode,startDate, endDate, Order.pay_status_success);
        List<Order> orderList = orderRepository.findAll(getSpecification(dto));
        List<OrderExportInfoDto> orderInfoDtos = orderList.stream().map(entity -> {
            OrderExportInfoDto orderInfoDto = new OrderExportInfoDto();
            orderInfoDto.setBody(entity.getBody());
            orderInfoDto.setDetails(entity.getDetails());
            orderInfoDto.setId(entity.getId());
            orderInfoDto.setOutTradeNo(entity.getOutTradeNo());
            orderInfoDto.setPaymentDate(DateUtils.getFormatDate(entity.getPaymentDate(), DateUtils.FULL_DATE_FORMAT));
            orderInfoDto.setPayStatus(entity.getPayStatus());
            orderInfoDto.setTotalMoney(entity.getTotalMoney());

            OrderShip orderShip = entity.getOrderShip();
            orderInfoDto.setArea(orderShip.getArea());
            orderInfoDto.setCity(orderShip.getCity());
            orderInfoDto.setConsigneeAddress(orderShip.getConsigneeAddress());
            orderInfoDto.setPhone(orderShip.getPhone());
            orderInfoDto.setProvince(orderShip.getProvince());
            orderInfoDto.setRemark(orderShip.getRemark());
            orderInfoDto.setSex(orderShip.getSex());
            orderInfoDto.setTel(orderShip.getTel());
            orderInfoDto.setConsigneeName(orderShip.getConsigneeName());
            if (entity.getWxpayNotify() != null) {
                orderInfoDto.setTransactionId(entity.getWxpayNotify().getTransactionId());
            }
            return orderInfoDto;
        }).collect(Collectors.toList());
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), OrderExportInfoDto.class, orderInfoDtos);
        workbook.write(response.getOutputStream());
    }

}

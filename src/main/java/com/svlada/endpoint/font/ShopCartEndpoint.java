package com.svlada.endpoint.font;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.request.CustomResponseStatus;
import com.svlada.component.repository.MajorImageRepository;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.ProductRepository;
import com.svlada.component.repository.ShopCartRepository;
import com.svlada.endpoint.dto.ShopCartDto;
import com.svlada.endpoint.dto.builder.ShopCartBuilder;
import com.svlada.entity.ShopCart;
import com.svlada.entity.User;
import com.svlada.entity.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/font/shopCart")
public class ShopCartEndpoint {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShopCartRepository shopCartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MajorImageRepository majorImageRepository;

    /**
     * 添加到购物车，修改购物车
     */
    @PutMapping(value = "/set")
    public CustomResponse add(@RequestBody ShopCartDto dto) {
        Product product = productRepository.getOne(dto.getProductId());
        if (StringUtils.isEmpty(product)) {
            return fail(CustomResponseStatus._40000, "商品不存在!");
        }
        if (Product.STATUS_DOWN.equals(product.getStatus())) {
            return fail(CustomResponseStatus._40401, "商品已经下架了!");
        }
        User user = WebUtil.getCurrentUser();
        ShopCart shopCart = shopCartRepository.findOneByUserIdAndProduct(user.getId(), product.getId());
        if (shopCart == null) {//购物车中无该商品
            shopCart = new ShopCart();
            shopCart.setUserId(user.getId());
            shopCart.setProductId(dto.getProductId());
            shopCart.setCreateDate(new Date());
            shopCart.setNumber(dto.getNumber());
        } else {//购物车中已经有该商品，则添加数量
            shopCart.setNumber(shopCart.getNumber() + dto.getNumber());
        }
        shopCartRepository.save(shopCart);
        return success();
    }

    /**
     * 删除购物车的某些商品
     *
     * @param productIds
     * @return
     */
    @DeleteMapping("/remove")
    public CustomResponse removeProduct(@RequestParam(name = "productIds") Long[] productIds) {
        User user = WebUtil.getCurrentUser();
        shopCartRepository.deleteAllByUserIdAndProductIdIn(user.getId(), productIds);
        return success();
    }

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 获取用户的购物车中的商品信息:商品名称，现价，数量，是否包邮，首页图片，库存 用户选择的数量
     *
     * @return
     */
    @GetMapping(value = "/list")
    public CustomResponse list() {
        User user = WebUtil.getCurrentUser();
        LOGGER.info("当前登录的用户为 user:{}",user.getNickName());
        List<ShopCart> shopCarts = shopCartRepository.findAllByUserId(user.getId());
        List<ShopCartDto> shopCartDtos = ShopCartBuilder.buildShopCartDtos(shopCarts);
        return success(shopCartDtos);

    }

}

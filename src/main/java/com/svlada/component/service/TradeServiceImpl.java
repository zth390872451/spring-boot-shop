package com.svlada.component.service;

import com.svlada.common.utils.wx.TimeUtil;
import com.svlada.component.repository.*;
import com.svlada.endpoint.dto.TradeDTO;
import com.svlada.entity.Order;
import com.svlada.entity.OrderItem;
import com.svlada.entity.User;
import com.svlada.entity.WxpayNotify;
import com.svlada.entity.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static com.svlada.entity.Order.pay_status_init;
import static com.svlada.entity.Order.pay_status_success;

@Transactional
@Service
public class TradeServiceImpl implements TradeService{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private WxpayNotifyRepository wxpayNotifyRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(TradeServiceImpl.class);

    @Override
    public TradeDTO wxpay(double balance, String subject, String body, Long price) {
        return null;
    }

    @Override
    public void processWxpayNotify(WxpayNotify wxpayNotify) {
        Order order = orderRepository.findOneByOutTradeNo(wxpayNotify.getOutTradeNo());
        if (order!=null && order.getPayStatus().equals(pay_status_init)){
            order.setPayStatus(pay_status_success);
            User user = order.getUser();
            if (!user.getMember()){
                user.setMember(true);
                userRepository.save(user);
            }
            WxpayNotify orderWxpayNotify = order.getWxpayNotify();
            if (orderWxpayNotify==null){//没有关联关系,建立
                orderWxpayNotify = wxpayNotify;
            }
            log.info("保存回调记录");
            wxpayNotifyRepository.save(wxpayNotify);
            log.info("更新订单的状态");
            order.setWxpayNotify(wxpayNotify);
            order.setPayStatus(pay_status_success);
            order.setPaymentDate(TimeUtil.getDateStrTo(wxpayNotify.getTimeEnd(), TimeUtil.FORMAT_YMD_HMS));
            log.info("更新订单的状态");
            orderRepository.save(order);
            log.info("更新库存、销量");
            //更新库存、销量()
            Set<OrderItem> items = order.getItems();
            Set<Product> products = new HashSet<>();
            for (OrderItem orderItem:items){
                Product product = orderItem.getProduct();
                log.info("更新库存、product:{}",product.getName());
                product.setStock(product.getStock()-orderItem.getNumber());
                product.setSaleCount(product.getSaleCount()+orderItem.getNumber());
                products.add(product);
            }
            productRepository.save(products);
        }else {
            log.debug("查无此订单OutTradeNo，{}",wxpayNotify.getOutTradeNo());
        }
    }
}

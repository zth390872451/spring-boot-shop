package com.svlada.component.service;

import com.svlada.component.repository.OrderItemRepository;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.OrderShipRepository;
import com.svlada.component.repository.ProductRepository;
import com.svlada.entity.Order;
import com.svlada.entity.OrderItem;
import com.svlada.entity.OrderShip;
import com.svlada.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderShipRepository orderShipRepository;

    public Order save(Set<Product> products, Set<OrderItem> orderItems, Order order,OrderShip orderShip){
        //库存,销量变更
        productRepository.save(products);

        //订单生成
        orderRepository.save(order);

        //订单详情记录生成
        orderItems.stream().filter(orderItem -> orderItem!=null).forEach(orderItem -> orderItem.setOrder(order));
        Set<OrderItem> orderItemSet = new HashSet<>();
        for (OrderItem orderItem:orderItems){
            orderItem.setOrder(order);
            orderItemSet.add(orderItem);
        }
        orderItemRepository.save(orderItemSet);
        //配送记录的生成(在订单记录生成之后)
        orderShip.setOrder(order);
        orderShipRepository.save(orderShip);
        return order;
    }

}

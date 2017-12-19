package com.svlada.component.service;

import com.svlada.entity.Order;
import com.svlada.entity.OrderItem;
import com.svlada.entity.OrderShip;
import com.svlada.entity.product.Product;

import java.util.Set;

public interface OrderService {

    Order save(Set<Product> products, Set<OrderItem> orderItems, Order order, OrderShip orderShip);
}

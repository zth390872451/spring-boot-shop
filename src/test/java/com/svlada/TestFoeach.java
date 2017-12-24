package com.svlada;

import com.svlada.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class TestFoeach {

    public static void main(String[] args) {
        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setId(1L);
        Order order1 = new Order();
        order1.setId(2L);
        orderList.add(order);
        orderList.add(order1);
        System.out.println("order1 = " + order1);
        for (Order each:orderList){
            each.setExportFlag(true);
        }
        System.out.println("orderList = " + orderList);
    }
}

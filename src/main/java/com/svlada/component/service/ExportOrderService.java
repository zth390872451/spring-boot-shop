package com.svlada.component.service;

import com.svlada.component.repository.OrderRepository;
import com.svlada.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ExportOrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void exportOrder(List<String> orders) {
        if (!CollectionUtils.isEmpty(orders)) {
            orderRepository.updateExportFlag(orders);
        }
    }

    public void export(List<Order> orders) {
        for (Order order : orders) {
            order.setExport(1);
        }
        orderRepository.save(orders);
    }
}

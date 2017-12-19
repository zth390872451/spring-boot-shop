package com.svlada.endpoint.font;

import com.svlada.common.request.CustomResponse;
import com.svlada.entity.Order;
import com.svlada.component.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/font/report")
public class ReportStaticEndpoint {

    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("/get/order")
    public CustomResponse getOrder(@PathVariable("id") Long id){
        Order order = orderRepository.findOne(id);
        return success(order);
    }

    @GetMapping("/get/product")
    public CustomResponse getProduct(@PathVariable("id") Long id){
        Order order = orderRepository.findOne(id);
        return success(order);
    }



}

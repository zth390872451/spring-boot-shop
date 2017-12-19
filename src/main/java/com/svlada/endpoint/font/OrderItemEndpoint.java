package com.svlada.endpoint.font;

import com.svlada.common.request.CustomResponse;
import com.svlada.entity.Order;
import com.svlada.endpoint.dto.OrderDto;
import com.svlada.component.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/font/orderItem")
public class OrderItemEndpoint {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping(value = "/add")
    public CustomResponse add(@RequestBody OrderDto dto) {
        Order order = new Order();
        orderRepository.save(order);
        return success();
    }

    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id){
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
        Order order= orderRepository.findOne(dto.getId());
        orderRepository.save(order);
        return success();
    }

}

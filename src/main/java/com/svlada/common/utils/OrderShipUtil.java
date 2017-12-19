package com.svlada.common.utils;

import com.svlada.endpoint.dto.OrderDto;
import com.svlada.entity.Address;
import com.svlada.entity.OrderShip;

public class OrderShipUtil {

    public static OrderShip getOrderShip(OrderDto orderDto, Address address) {
        OrderShip orderShip = new OrderShip();
        orderShip.setConsigneeName(address.getName());//收货人
        orderShip.setConsigneeAddress(address.getAddress());//收货的详细地址
        orderShip.setArea(address.getArea());
        orderShip.setCity(address.getCity());
        orderShip.setPhone(address.getPhone());
        orderShip.setProvince(address.getProvince());
        orderShip.setTel(address.getMobile());
        orderShip.setZip(address.getZip());
        orderShip.setRemark(orderDto.getRemark());
        return orderShip;
    }
}

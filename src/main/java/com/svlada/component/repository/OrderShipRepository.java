package com.svlada.component.repository;

import com.svlada.entity.OrderShip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderShipRepository extends JpaRepository<OrderShip, Long> {

    OrderShip findOneByOrderId(Long orderId);
}

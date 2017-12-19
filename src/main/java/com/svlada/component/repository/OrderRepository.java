package com.svlada.component.repository;

import com.svlada.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>  , JpaSpecificationExecutor<Order> {

    Order findOneByOutTradeNo(String outTradeNo);

    List<Order> findOneByUserId(String userId);

    List<Order> findOneByUserIdAndPayStatus(Long userId, Integer payStatus);

    List<Order> findByShareIdAndPayStatus(String shareId, Integer payStatus);

    @Modifying
    @Transactional
    @Query(value = "update user_order  set share_flag = ?2 where pay_status =1 and shareId is not null and shareFlag =FALSE and out_trade_no in (?1)",nativeQuery = true)
    void updateShareFlag(List<String> ids, Boolean shareFlag);
}

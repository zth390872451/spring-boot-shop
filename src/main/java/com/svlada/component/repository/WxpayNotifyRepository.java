package com.svlada.component.repository;

import com.svlada.entity.Order;
import com.svlada.entity.WxpayNotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WxpayNotifyRepository extends JpaRepository<WxpayNotify, Long>  , JpaSpecificationExecutor<WxpayNotify> {

    WxpayNotify findOneByOutTradeNo(String outTradeNo);
}

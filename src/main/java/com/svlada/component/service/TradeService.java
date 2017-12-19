package com.svlada.component.service;

import com.svlada.endpoint.dto.TradeDTO;
import com.svlada.entity.WxpayNotify;


public interface TradeService {
	public TradeDTO wxpay(double balance, String subject, String body, Long price);

	public void processWxpayNotify(WxpayNotify wxpayNotify) ;
}

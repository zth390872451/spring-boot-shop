package com.svlada;

import com.svlada.common.utils.wx.WxPayUtil;
import com.svlada.endpoint.dto.TradeDTO;
import org.junit.Test;

public class WxPayUtilTest {

    @Test
    public void unifiedOrder(){
        WxPayUtil wxPayUtil = new WxPayUtil();
        String outTradeNo = "098f6bcd4621d373cade4e832627b001";
        String body = "朗姆酒，美味的酒";
        String detail = "天山出品，绝对精品";
        int totalFee = 100;
        String ip = "14.153.126.29";
        String notifyUrl = "http://www.pcjshe.com/wxpay/callback";
        TradeDTO tradeDTO = wxPayUtil.unifiedOrder(outTradeNo, body, detail, totalFee, ip, notifyUrl);
        System.out.println("tradeDTO = " + tradeDTO);
    }
}

package com.svlada.endpoint.font;

import com.svlada.common.utils.wx.WxCommonUtil;
import com.svlada.common.utils.wx.XMLUtil;
import com.svlada.component.service.TradeService;
import com.svlada.config.WxConfig;
import com.svlada.entity.WxpayNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xmc
 * sim卡充值
 */
@Controller
@RequestMapping("/callback")
public class CallbackEndPoint {
	private static final Logger log = LoggerFactory.getLogger(CallbackEndPoint.class);
	@Autowired
    private TradeService tradeService;

	
	List<Double> amountList = new ArrayList<Double>(Arrays.asList(10.0, 30.0, 50.0, 100.0, 200.0));
	//List<Double> amountList = new ArrayList<Double>(Arrays.asList(0.01, 0.02, 0.03, 0.04, 0.05));
	



	/*public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		String str = "buyer_id=2088612088211410, trade_no=2016070521001004410235789318, body=13760280656-充值-0.01, use_coupon=N, notify_time=2016-07-05 15:28:03, subject=777777777777771-充值, sign_type=RSA, is_total_fee_adjust=N, notify_type=trade_status_sync, out_trade_no=20160705151413318000, gmt_payment=2016-07-05 15:14:21, trade_status=TRADE_SUCCESS, discount=0.00, buyer_email=13128795958, gmt_create=2016-07-05 15:14:20, price=0.01, total_fee=0.01, quantity=1, seller_id=2088421214153555, notify_id=adea32a3cddf79a9a87ccbcfd342d69j5y, seller_email=weixiaobao_zhifubao@umeox.com, payment_type=1";
		String[] strArr = str.split(",");
		for (String string : strArr) {
			String[] str2 = string.trim().split("=");
			params.put(str2[0], str2[1]);
		}
		params.put("sign", "p6RYPgNRFaWt+ZQOFKuFcL5njTcuIVB2oy+sUuHKrvxkqEg3OP3wY4Ef9GAK0dmeksAKTfB4Y3bnN7+EeBBTp+KtETLZqniCcR76pLQh3zhLu38mAQxSCtREUpayv3P7LBlWEbiiFgEmj5CPoqztw2OHbDsqCn5bA5wDsRQ/HZE=");
		for (Map.Entry<String, String> entry : params.entrySet()) {  
		    System.out.println(entry.getKey() + ":" + entry.getValue());  
		} 
		System.out.println(AlipayNotifyHelper.verify(params));
	}*/


	/**
	 * wxpay回调
	 */
	@RequestMapping(value = "/wx",method = RequestMethod.POST)
	public String wxpayCallBack(HttpServletRequest request){
		try {
			String responseStr = parseWeixinCallback(request);
			Map<String, Object> map = XMLUtil.doXMLParse(responseStr);
			//验证签名
			log.debug("wxpay callback - pamas:{}",map.toString());
			String outTradeNo = (String)map.get("out_trade_no");
			if (!WxCommonUtil.checkIsSignValidFromResponseString(responseStr)) {
				log.debug("1. wxpay callback - invalid sign,outTradeNo:{}",outTradeNo);
				return WxCommonUtil.setXML(WxConfig.FAIL, "invalid sign");
			}
			if (WxConfig.FAIL.equalsIgnoreCase(map.get("result_code").toString())) {
				log.debug("2. weixin callback - pay fail,outTradeNo:{}",outTradeNo);
				return WxCommonUtil.setXML(WxConfig.FAIL, "weixin pay fail");
			}
			if (WxConfig.SUCCESS.equalsIgnoreCase(map.get("result_code").toString())) {
				WxpayNotify payNotify = getWxpayNotify(map);
				tradeService.processWxpayNotify(payNotify);
				log.debug("3. weixin callback - pay success,outTradeNo:{}",outTradeNo);
				return WxCommonUtil.setXML(WxConfig.SUCCESS,"OK");
			}
		} catch (Exception e) {
			log.debug("4. weixin pay - fail,异常：{},\n 异常：{} " + e.getMessage(),e);
			return WxCommonUtil.setXML(WxConfig.FAIL,"weixin pay server exception");
		}
		return WxCommonUtil.setXML(WxConfig.FAIL, "weixin pay fail");
	}
	
	@RequestMapping(value = "/testWx",method = RequestMethod.GET)
	public String wx(){
		WxpayNotify notify = new WxpayNotify("4007792001201607189249102553", "20170718175151", 1000, "CFT");
		notify.setOutTradeNo("20171027155125767000");
		notify.setMchId("1364053502");
		tradeService.processWxpayNotify(notify);
		return "ok";
	}
	
	/**
	 * 解析微信回调参数
	 */
	private String parseWeixinCallback(HttpServletRequest request) throws IOException {
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
		return result;
	}
	
	private WxpayNotify getWxpayNotify(Map<String, Object> map){
		WxpayNotify notify = new WxpayNotify((String)map.get("transaction_id"), (String)map.get("time_end"), Integer.valueOf(map.get("total_fee").toString()), (String)map.get("bank_type"));
		notify.setOutTradeNo((String)map.get("out_trade_no"));
		notify.setMchId((String)map.get("mch_id"));
		return notify;
	}
}

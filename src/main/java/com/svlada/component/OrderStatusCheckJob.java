package com.svlada.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.svlada.common.utils.wx.WxPayUtil;
import com.svlada.config.WxConfig;
import com.svlada.common.utils.wx.HttpsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static com.svlada.config.WxConfig.ACCESS_TOKEN_URL;


@Component
public class OrderStatusCheckJob implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(WxPayUtil.class);


	// cron表达式：每个一小时执行一次
//	@Scheduled(cron="0 0 */1 * * ?")
//	@Scheduled(cron="0 */1 * * * ?")
	public void getAccessToken(){
		try {
			String response = HttpsUtil.httpsRequestToString(ACCESS_TOKEN_URL, "GET", null);
			JSONObject jsonObject = JSON.parseObject(response);
			String access_token = jsonObject.getString("access_token");
			logger.info("请求到的 jsonObject:{}", jsonObject.toJSONString());
			if (access_token!=null){
				WxConfig.ACCESS_TOKEN = access_token;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void run(String... strings) throws Exception {
		WeixinThread weixinThread = new WeixinThread();
		Thread thread = new Thread(weixinThread);
		thread.start();
	}
}

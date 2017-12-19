package com.svlada.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.svlada.config.WxConfig;
import com.svlada.endpoint.wechat.AccessToken;
import com.svlada.endpoint.wechat.JsapiTicket;
import com.svlada.common.utils.wx.HttpsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class WeixinThread implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(WeixinThread.class);

    // 第三方用户唯一凭证
    public static String appid = "";
    // 第三方用户唯一凭证密钥
    public static String appsecret = "";
    public static AccessToken accessToken = null;
    public static JsapiTicket jsapiTicket = null;

    @Override
    public void run() {
        while (true) {
            try {
                accessToken =getTokenNow();
                if (null != accessToken) {
                    log.info("获取access_token成功，有效时长" + accessToken.getExpiresIn() + "秒 token:" + accessToken.getToken());
                    try {
                        jsapiTicket = getTicketNow(accessToken.getToken());
                        if (jsapiTicket != null) {
                            log.info("获取jsapiTicket成功，有效时长" + jsapiTicket.getExpiresIn() + "秒 ticket:" + jsapiTicket.getTicket());
                        }
                    } catch (Exception e) {
                        // 如果jsapiTicket为null，60秒后再获取
                        Thread.sleep(60 * 1000);
                    }
                    // 休眠7000秒
                    Thread.sleep((accessToken.getExpiresIn() - 200) * 1000);
                } else {
                    // 如果access_token为null，60秒后再获取
                    Thread.sleep(60 * 1000);
                }
            } catch (InterruptedException e) {
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e1) {
                    e.printStackTrace();
                }
                log.info("异常 e:{}",e);
            }
        }
    }

    public static AccessToken getTokenNow(){
        String response = HttpsUtil.httpsRequestToString(WxConfig.ACCESS_TOKEN_URL, "GET", null);
        JSONObject jsonObject = JSON.parseObject(response);
        log.info("请求到的 token now:{}", jsonObject.toJSONString());
        String access_token = jsonObject.getString("access_token");
        String expiresIn = jsonObject.getString("expires_in");
        if (!StringUtils.isEmpty(access_token)){
            AccessToken newToken = new AccessToken();
            newToken.setToken(access_token);
            newToken.setExpiresIn(Integer.valueOf(expiresIn));
            WeixinThread.accessToken = newToken;
        }
        return WeixinThread.accessToken;
    }

    public static JsapiTicket getTicketNow(String accessToken){
        String ticketUrl = WxConfig.TICKET_URL.replace("ACCESS_TOKEN",accessToken);
        String response = HttpsUtil.httpsRequestToString(ticketUrl, "GET", null);
        JSONObject jsonObject = JSON.parseObject(response);
        log.info("请求到的 TICKET now:{}", jsonObject.toJSONString());
        String ticket = jsonObject.getString("ticket");
        String expiresIn = jsonObject.getString("expires_in");
        if (!StringUtils.isEmpty(ticket)){
            JsapiTicket jsapiTicketNew = new JsapiTicket();
            jsapiTicketNew.setTicket(ticket);
            jsapiTicketNew.setExpiresIn(Integer.valueOf(expiresIn));
            WeixinThread.jsapiTicket = jsapiTicketNew;
        }
        return WeixinThread.jsapiTicket;
    }
}



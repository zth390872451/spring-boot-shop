package com.svlada.common.utils.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfoUtil {

    public static final Logger logger = LoggerFactory.getLogger(UserInfoUtil.class);

    // 1.获取code的请求地址
    public static String Get_Code = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STAT#wechat_redirect";
    // 替换字符串
    public static String getCode(String APPID, String REDIRECT_URI,String SCOPE) {
        return String.format(Get_Code,APPID,REDIRECT_URI,SCOPE);
    }

    // 2.获取Web_access_tokenhttps的请求地址
    public static String Web_access_tokenhttps = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    // 替换字符串
    public static String getWebAccess(String APPID, String SECRET,String CODE) {
        return String.format(Web_access_tokenhttps, APPID, SECRET,CODE);
    }

    // 3.拉取用户信息的请求地址
    public static String User_Message = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    // 替换字符串
    public static String getUserMessage(String access_token, String openid) {
        return String.format(User_Message, access_token,openid);
    }

    // 替换字符串
    public static String getSUBSCRIBE_URL(String access_token, String openid) {
        return String.format(SUBSCRIBE_URL, access_token,openid);
    }
    public static String GET_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

//    public  static String ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    public  static String SUBSCRIBE_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

    public static Boolean isSubscribe(String openId){
        try {
            String accessToken = WxCommonUtil.getAccessToken();
            String url = getSUBSCRIBE_URL(accessToken,openId);
            String response = HttpsUtil.httpsRequestToString(url, "GET", null);
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject!=null){
                Integer subscribe = jsonObject.getInteger("subscribe");
                logger.info("是否关注公众号，1：关注，0：未关注! subscribe=:{}",subscribe);
                if (subscribe!=null){
                    return subscribe==1?true:false;
                }else {
                    return false;
                }
            }
        }catch (Exception e){
            logger.error("查询关注公众号 Exception",e);
        }
        return false;
    }
    public static void main(String[] args) {
        /*String REDIRECT_URI = "http%3A%2F%2Fhttp://www.pcjshe.com";
        String SCOPE = "snsapi_userinfo"; // snsapi_userinfo // snsapi_login

        //appId
        String appId = "wx6aef1915818229a5";

        String getCodeUrl = getCode(appId, REDIRECT_URI, SCOPE);
        System.out.println("getCodeUrl:"+getCodeUrl);*/
        String accessToken = WxCommonUtil.getAccessToken();
        System.out.println("accessToken = " + accessToken);
        String openId= "oKMbG0XgCmHqGrSN8GGZ1Zk4yhBw";
        String url = getSUBSCRIBE_URL(accessToken,openId);
        String response = HttpsUtil.httpsRequestToString(url, "GET", null);
        System.out.println("response = " + response);
    }
}

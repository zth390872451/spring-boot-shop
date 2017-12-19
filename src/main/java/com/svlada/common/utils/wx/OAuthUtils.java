package com.svlada.common.utils.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.svlada.common.WebUtil;
import com.svlada.common.utils.ApplicationSupport;
import com.svlada.component.repository.UserRepository;
import com.svlada.config.WxConfig;
import com.svlada.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class OAuthUtils {

    private  static Logger logger = LoggerFactory.getLogger(OAuthUtils.class);

    public static String oauth2(String targetUrl,String code) {
        if (!StringUtils.isEmpty(code)) {
            String CODE = code;
            String WebAccessToken = "";
            String openId = "";
            String nickName, sex, openid = "";
            String SCOPE = "snsapi_userinfo";
            String getCodeUrl = UserInfoUtil.getCode(WxConfig.APP_ID, targetUrl, SCOPE);
            logger.info("第一步:用户授权, get Code URL:{}", getCodeUrl);

            // 替换字符串，获得请求access token URL
            String tokenUrl = UserInfoUtil.getWebAccess(WxConfig.APP_ID, WxConfig.API_KEY, CODE);
            logger.info("第二步:get Access Token URL:{}", tokenUrl);

            // 通过https方式请求获得web_access_token
            String responseBody = HttpsUtil.httpsRequestToString(tokenUrl, "GET", null);

            JSONObject jsonObject = JSON.parseObject(responseBody);
            logger.info("请求到的Access Token:{}", jsonObject.toJSONString());
            if (null != jsonObject) {
                try {
                    WebAccessToken = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                    logger.info("获取access_token成功!");
                    logger.info("WebAccessToken:{} , openId:{}", WebAccessToken, openId);

                    // 3. 使用获取到的 Access_token 和 openid 拉取用户信息
                    String userMessageUrl = UserInfoUtil.getUserMessage(WebAccessToken, openId);
                    logger.info("第三步:获取用户信息的URL:{}", userMessageUrl);

                    // 通过https方式请求获得用户信息响应
                    String userMessageResponse = HttpsUtil.httpsRequestToString(userMessageUrl, "GET", null);

                    JSONObject userMessageJsonObject = JSON.parseObject(userMessageResponse);

                    logger.info("用户信息:{}", userMessageJsonObject.toJSONString());

                    if (userMessageJsonObject != null) {
                        try {
                            //用户昵称
                            nickName = userMessageJsonObject.getString("nickname");
                            //用户性别
                            sex = userMessageJsonObject.getString("sex");
                            sex = ("1".equals(sex)) ? "男" : "女";
                            //用户唯一标识
                            openid = userMessageJsonObject.getString("openid");
                            String headImgUrl = userMessageJsonObject.getString("headimgurl");
                            String province = userMessageJsonObject.getString("province");
                            String city = userMessageJsonObject.getString("city");

                            logger.info("用户昵称:{}", nickName);
                            logger.info("用户性别:{}", sex);
                            logger.info("OpenId:{}", openid);

                            UserRepository userRepository = ApplicationSupport.getBean(UserRepository.class);
                            User user = userRepository.findOneByOpenId(openId);
                            if (user==null){
                                user = new User();
                            }
                            user.setWebAccessToken(WebAccessToken);
                            user.setHeadImgUrl(headImgUrl);
                            user.setSex(sex);
                            user.setProvince(province);
                            user.setCity(city);
                            user.setOpenId(openId);
                            user.setUsername(openId);
                            user.setNickName(nickName);
                            user.setSex(sex);
                            user.setProvince(province);
                            user.setCity(city);
                            userRepository.save(user);
                            //设置当前用户到线程中
                            WebUtil.setCurrentUser(user);
                            return openid;
                        } catch (JSONException e) {
                            logger.error("获取用户信息失败");
                        }
                    }
                } catch (JSONException e) {
                    logger.error("获取Web Access Token失败");
                }
            }
        }
        return null;
    }

}

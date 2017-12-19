package com.svlada.filter;


import com.svlada.common.WebUtil;
import com.svlada.common.utils.wx.OAuthUtils;
import com.svlada.config.WxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * 微信授权 拦截器处理类
 *
 * @creator ZTH
 * @modifier ZTH
 * @date：2017-05-19
 */
public class ApiLimitInterceptorHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(ApiLimitInterceptorHandler.class);

    /**
     * @param request
     * @param response
     * @param handler
     * @return
     */
    public static boolean ApiLimitHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Enumeration em = request.getParameterNames();
        Map<String,Object> params = new HashMap<>();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            String value = request.getParameter(name);
            params.put(name,value);
            LOGGER.info("请求参数 name:{},value{}", name, value);
        }
        LOGGER.info("请求参数 params:{}", params);
        String requestURI = request.getRequestURI();//半路径
        if (requestURI.startsWith("/drink")) {//需要微信授权的页面
            String code = request.getParameter("code");
            String requestURL = request.getRequestURL().toString();
            String encodeUrl = URLEncoder.encode(requestURL);//编码后的全路径
            LOGGER.info("需要微信授权的页面！");
            if (StringUtils.isEmpty(code)) {
                LOGGER.info("未授权,重定向到授权页面");
                LOGGER.info("请求参数 encodeUrl:{}", encodeUrl);
                /*String shareOpenId = request.getParameter("shareOpenId");
                LOGGER.info("分享人 shareOpenId:{}", shareOpenId);
                if (!StringUtils.isEmpty(shareOpenId)){
                    encodeUrl =encodeUrl+"?shareOpenId="+shareOpenId;
                    LOGGER.info("添加分享人参数 shareOpenId:{},\n\t encodeUrl:{}", shareOpenId,encodeUrl);
                }*/
                String targetUrl = String.format(WxConfig.WEIXIN_MANUAL_URL, encodeUrl);//授权url
                LOGGER.info("即将跳转的页面 targetUrl:{}", targetUrl);
                response.sendRedirect(targetUrl);
                LOGGER.info("sendRedirect调用完毕");
                return false;
            } else {//已经获取了授权code
                LOGGER.info("已经获取了授权code={},准备获取用户信息", code);
                OAuthUtils.oauth2(requestURL, code);
//                requestURL +="&openId="+ WebUtil.getCurrentUser().getOpenId()+"&jwtToken="+WebUtil.getCurrentUser().getJwtToken();
                LOGGER.info("成功获取到了用户信息，进入目标接口!");
                if (request.getParameter("shareOpenId")!=null){
                    String shareOpenId = request.getParameter("shareOpenId");
                    WebUtil.bindShare(shareOpenId,params);
                }
                /*if (StringUtils.isEmpty(openId)){
                    LOGGER.error("用户信息获取异常!");
                }else {
                    LOGGER.info("查看是否关注公众号!");
                    Boolean subscribe = UserInfoUtil.isSubscribe(openId);
                    if (subscribe){
                        LOGGER.info("已关注公众号!");
                    }else {
                        String url = String.format(ConstantConfig.index_subscribe,subscribe);
                        LOGGER.info("未关注公众号!跳转引导页面！url={}",url);
                        response.sendRedirect(url);
                        return false;
                    }
                }*/
                return true;
            }
        } else {//不需要微信授权的页面
            return true;
        }
    }

}

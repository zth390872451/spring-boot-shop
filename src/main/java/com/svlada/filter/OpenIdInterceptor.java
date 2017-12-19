package com.svlada.filter;

import com.svlada.common.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API访问拦截器
 *
 * @creator ZTH
 * @modifier ZTH
 * @date：2017-05-19
 */
public class OpenIdInterceptor extends HandlerInterceptorAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(OpenIdInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        /*User currentUser = WebUtil.getCurrentUser();
        if (currentUser!=null){
            LOGGER.info("当前线程的ID:{}，移除用户信息,user:",Thread.currentThread().getId(),currentUser.getNickName());
            WebUtil.removeUser();
        }else {
            LOGGER.info("当前线程的ID:{},获取用户信息,无用户信息！",Thread.currentThread().getId());
        }*/
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        LOGGER.info("当前线程的ID:{}，请求结束，移除用户信息!",Thread.currentThread().getId());
        WebUtil.removeUser();
    }
}

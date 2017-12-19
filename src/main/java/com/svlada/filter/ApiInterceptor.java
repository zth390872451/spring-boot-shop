package com.svlada.filter;

import com.svlada.common.WebUtil;
import com.svlada.entity.User;
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
public class ApiInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return ApiLimitInterceptorHandler.ApiLimitHandler(request, response, handler);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        User currentUser = WebUtil.getCurrentUser();
        if (currentUser!=null){
            String openId = currentUser.getOpenId();
            response.setHeader("openId",openId);
        }
    }
}

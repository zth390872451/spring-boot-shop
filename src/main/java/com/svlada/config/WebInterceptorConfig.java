package com.svlada.config;

import com.svlada.filter.ApiInterceptor;
import com.svlada.filter.OpenIdInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebInterceptorConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 主要方法说明：
         * addPathPatterns 用于添加拦截规则
         * excludePathPatterns 用户排除拦截
         */
        registry.addInterceptor(new OpenIdInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/drink/*");
        super.addInterceptors(registry);
    }
}
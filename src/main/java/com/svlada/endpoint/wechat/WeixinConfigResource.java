package com.svlada.endpoint.wechat;

import com.svlada.common.request.CustomResponse;
import com.svlada.common.utils.wx.WxPayUtil;
import com.svlada.common.utils.wx.WxCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
public class WeixinConfigResource {

    private static final Logger log = LoggerFactory.getLogger(WxPayUtil.class);

    /**
     * @Description: 前端获取微信JSSDK的配置参数
     * @date 2016年3月19日 下午5:57:52
     */
    @RequestMapping("/jssdk")
    public CustomResponse JSSDK_config(
            @RequestParam(value = "url") String url,
            @RequestParam(value = "debug", required = false) Boolean debug) {
        try {
            log.info("wxpay sign valid fail,message :{}", "微信统一下单失败,签名可能被篡改");
            Map<String, String> configMap = WxCommonUtil.jsSDK_Sign(url,debug);
            log.error("wxpay sign valid fail,message :{}", "微信统一下单失败,签名可能被篡改");
            return success(configMap);
        } catch (Exception e) {
            return fail();
        }
    }

}

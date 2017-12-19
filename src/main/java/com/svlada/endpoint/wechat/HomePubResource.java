package com.svlada.endpoint.wechat;

import com.svlada.config.WxConfig;
import com.svlada.common.utils.wx.WxCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat")
public class HomePubResource {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 接收并校验四个请求参数
     * 微信发送请求数据验证服务器是否正确
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return echostr
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String checkName(@RequestParam(name = "signature") String signature,
                            @RequestParam(name = "timestamp") String timestamp,
                            @RequestParam(name = "nonce") String nonce,
                            @RequestParam(name = "echostr") String echostr) {
        System.out.println("-----------------------开始校验------------------------");
        //排序
        String sortString = WxCommonUtil.sort(WxConfig.TOKEN, timestamp, nonce);
        //加密
        String myString = WxCommonUtil.sha1(sortString);
        //校验
        if (myString != null && myString != "" && myString.equals(signature)) {
            System.out.println("签名校验通过");
            //如果检验成功原样返回echostr，微信服务器接收到此输出，才会确认检验完成。
            return echostr;
        } else {
            LOGGER.error("签名校验失败");
            return "签名校验失败!";
        }
    }

}

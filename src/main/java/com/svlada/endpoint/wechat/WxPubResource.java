package com.svlada.endpoint.wechat;

import com.svlada.common.utils.wx.WxCommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WxPubResource {

    //此处TOKEN即我们刚刚所填的token
    private String TOKEN = "weixin";

    /**
     * 接收并校验四个请求参数
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return echostr
     */
    @RequestMapping(value = "/wechat", method = RequestMethod.GET)
    public String checkName(@RequestParam(name = "signature") String signature,
                            @RequestParam(name = "timestamp") String timestamp,
                            @RequestParam(name = "nonce") String nonce,
                            @RequestParam(name = "echostr") String echostr) {
        System.out.println("-----------------------开始校验------------------------");
    //排序
        String sortString = WxCommonUtil.sort(TOKEN, timestamp, nonce);
    //加密
        String myString = WxCommonUtil.sha1(sortString);
    //校验
        if (myString != null && myString != "" && myString.equals(signature)) {
            System.out.println("签名校验通过");
    //如果检验成功原样返回echostr，微信服务器接收到此输出，才会确认检验完成。
            return echostr;
        } else {
            System.out.println("签名校验失败");
            return "签名校验失败!";
        }
    }

}

package com.svlada.endpoint.wechat;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.utils.WXRequestUtil;
import com.svlada.common.utils.wx.PayUtil;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.PartnerRepository;
import com.svlada.component.repository.UserRepository;
import com.svlada.component.repository.WxpayNotifyRepository;
import com.svlada.config.ConstantConfig;
import com.svlada.entity.Order;
import com.svlada.entity.User;
import com.svlada.entity.WxpayNotify;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.svlada.common.request.CustomResponseBuilder.success;
import static com.svlada.config.ConstantConfig.font_host;

/**
 * 需要进行微信授权的页面
 * 主页 drink/index  授权成功，后端跳转 81/drink/index
 * 地址 drink/address  授权成功，后端跳转 81/drink/address
 * 购物车 drink/car  授权成功，后端跳转 81/drink/car
 * 个人中心 drink/myself  授权成功，后端跳转 81/drink/myself
 */
@Controller
@RequestMapping("/drink")
public class RedirectResource {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/index")
    public void index(HttpServletResponse response) throws IOException {
        User currentUser = WebUtil.getCurrentUser();
        String openId = currentUser.getOpenId();
        String jwtToken = WebUtil.createTokenByOpenId(openId);
        String redirect = String.format(ConstantConfig.index, openId, jwtToken);
        LOGGER.info("index openId={}", openId);
        LOGGER.info("index jwtToken={}", jwtToken);
        LOGGER.info("index redirect={}", redirect);
        response.sendRedirect(redirect);
    }

    @GetMapping("/address")
    public void address(HttpServletResponse response) throws IOException {
        LOGGER.info("address redirect={}", ConstantConfig.address);
        User currentUser = WebUtil.getCurrentUser();
        String openId = currentUser.getOpenId();
        String jwtToken = WebUtil.createTokenByOpenId(openId);
        String redirect = String.format(ConstantConfig.address, openId, jwtToken);
        LOGGER.info("address redirect={}", redirect);
        response.sendRedirect(redirect);
    }

    @GetMapping("/car")
    public void car(HttpServletResponse response) throws IOException {
        LOGGER.info("car redirect={}", ConstantConfig.car);
        User currentUser = WebUtil.getCurrentUser();
        String openId = currentUser.getOpenId();
        String jwtToken = WebUtil.createTokenByOpenId(openId);
        String redirect = String.format(ConstantConfig.address, openId, jwtToken);
        LOGGER.info("car redirect={}", redirect);
        response.sendRedirect(redirect);
    }

    @GetMapping("/myself")
    public void myself(HttpServletResponse response) throws IOException {
        LOGGER.info("address redirect={}", ConstantConfig.myself);
        User currentUser = WebUtil.getCurrentUser();
        String openId = currentUser.getOpenId();
        String jwtToken = WebUtil.createTokenByOpenId(openId);
        String redirect = String.format(ConstantConfig.address, openId, jwtToken);
        LOGGER.info("address redirect={}", redirect);
        response.sendRedirect(redirect);
    }

    public static void main(String[] args) {

    }
    /**
     * 好友分享 功能
     */
    /*@GetMapping("/bindShare")
    public void bindShare(
            @RequestParam(value = "shareOpenId") String shareOpenId,
            @RequestParam(value = "targetUrl") String targetUrl,
            HttpServletResponse response) throws IOException {
        LOGGER.info("index shareOpenId={}", shareOpenId);
        LOGGER.info("index targetUrl={}", targetUrl);
        User currentUser = WebUtil.getCurrentUser();//点击链接的人
        User shareUser = userRepository.findOneByOpenId(shareOpenId);//分享链接的人
        if (shareUser == null) {
            response.sendRedirect(ConstantConfig.error_404);
        }
        Long userId = shareUser.getId();
        //查看该用户是否已经有过合作伙伴
        Partner partner = partnerRepository.findOneByUserIdAndShareUserId(userId,shareOpenId);
        if (partner == null) {//没有，则添加分享人为合作伙伴
            partner = new Partner();
            partner.setShareUser(shareUser);
            partner.setUserId(currentUser.getId());
            partnerRepository.save(partner);
        }else {//已经有合作伙伴不做处理
            LOGGER.info("{} was the user's partner!",currentUser.getOpenId());
        }
        String openId = currentUser.getOpenId();
        String jwtToken = WebUtil.createTokenByOpenId(openId);
        if (targetUrl.endsWith("/")){
            targetUrl = targetUrl.substring(0,targetUrl.length()-2);
        }
        String redirect =targetUrl+"?openId="+openId+"jwtToken="+jwtToken;
                LOGGER.info("index openId={}", openId);
        LOGGER.info("index jwtToken={}", jwtToken);
        LOGGER.info("index redirect={}", redirect);
        LOGGER.info("address redirect={}", ConstantConfig.index);
        response.sendRedirect(redirect);
    }*/

    /*@GetMapping("/bindShare")
    public void bindShare(
            @RequestParam(value = "shareOpenId") String shareOpenId,
            @RequestParam(value = "targetUrl") String targetUrl,
            HttpServletResponse response) throws IOException {
        LOGGER.info("分享人 shareOpenId={}", shareOpenId);
        LOGGER.info("分享的页面地址 targetUrl={}", targetUrl);
        User currentUser = WebUtil.getCurrentUser();//点击链接的人
        User shareUser = userRepository.findOneByOpenId(shareOpenId);//分享链接的人
        if (shareUser == null) {
            response.sendRedirect(ConstantConfig.error_404);
        }
        Long userId = shareUser.getId();
        //查看该用户是否已经有过合作伙伴
        Partner partner = partnerRepository.findOneByUserIdAndShareUserId(userId,shareOpenId);
        if (partner == null) {//没有，则添加分享人为合作伙伴
            partner = new Partner();
            partner.setShareUser(shareUser);
            partner.setUserId(currentUser.getId());
            partnerRepository.save(partner);
        }else {//已经有合作伙伴不做处理
            LOGGER.info("已经有合作伙伴！ {} ",currentUser.getOpenId());
        }
        String openId = currentUser.getOpenId();
        String jwtToken = WebUtil.createTokenByOpenId(openId);
        if (targetUrl.endsWith("/")){
            targetUrl = targetUrl.substring(0,targetUrl.length()-2);
        }
        String redirect =targetUrl+"?openId="+openId+"jwtToken="+jwtToken;
        LOGGER.info("index openId={}", openId);
        LOGGER.info("index jwtToken={}", jwtToken);
        LOGGER.info("index redirect={}", redirect);
        LOGGER.info("address redirect={}", ConstantConfig.index);
        response.sendRedirect(redirect);
    }
*/

    @GetMapping("/")
    public void login(@RequestParam(name = "code", required = false) String code,
                      @PathVariable(name = "url", required = false) String url,
                      @RequestParam(name = "state", required = false) String state,
                      RedirectAttributes redirectAttributes,
                      HttpServletResponse response) throws IOException {
        LOGGER.info("授权成功!");
        User currentUser = WebUtil.getCurrentUser();
        if (currentUser != null) {
            String jwtToken = WebUtil.createTokenByOpenId(currentUser.getOpenId());
            LOGGER.info("获取 jwtToken={}", jwtToken);
            response.sendRedirect(font_host+"/drink?openId=" + currentUser.getOpenId() + "&access_token=" + currentUser.getWebAccessToken() + "&jwtToken=" + jwtToken);
        } else {
            LOGGER.info("跳转index.html");
            response.sendRedirect("/index.html");
        }
    }


    /**
     * 微信网页授权流程:
     * 1. 用户同意授权,获取 code
     * 2. 通过 code 换取网页授权 access_token
     * 3. 使用获取到的 access_token 和 openid 拉取用户信息
     *
     * @param code  用户同意授权后,获取到的code
     * @param state 重定向状态参数
     * @return
     */
    /*@RequestMapping("/url")
    public String wechatLogin(@RequestHeader(name = "accessToken", required = false) String accessToken,
                              @RequestParam(name = "code", required = false) String code,
                              @RequestParam(name = "state", required = false, defaultValue = "STATE") String state
            , RedirectAttributes attributes) {
        if (StringUtils.isEmpty(code)) {//用户尚未授权
            LOGGER.info("用户尚未授权,准备进入提示授权页面.");
            return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6aef1915818229a5&redirect_uri=http%3A%2F%2Fwww.dsunyun.com%2Furl&response_type=code&scope=snsapi_userinfo&#wechat_redirect";
        }

        // 1. 用户同意授权,获取code
        LOGGER.info("收到微信重定向跳转.");
        LOGGER.info("用户同意授权,获取code:{} , state:{}", code, state);

        // 2. 通过code换取网页授权access_token
        if (!StringUtils.isEmpty(code)) {
            String APPID = WxConfig.APP_ID;
            String SECRET = WxConfig.API_KEY;
            String CODE = code;
            String WebAccessToken = "";
            String openId = "";
            String nickName, sex, openid = "";
            String REDIRECT_URI = WxConfig.REDIRECT_URI;
            String SCOPE = "snsapi_userinfo";

            String getCodeUrl = UserInfoUtil.getCode(APPID, REDIRECT_URI, SCOPE);
            LOGGER.info("第一步:用户授权, get Code URL:{}", getCodeUrl);

            // 替换字符串，获得请求access token URL
            String tokenUrl = UserInfoUtil.getWebAccess(APPID, SECRET, CODE);
            LOGGER.info("第二步:get Access Token URL:{}", tokenUrl);

            // 通过https方式请求获得web_access_token
            String response = HttpsUtil.httpsRequestToString(tokenUrl, "GET", null);

            JSONObject jsonObject = JSON.parseObject(response);
            LOGGER.info("请求到的Access Token:{}", jsonObject.toJSONString());
            if (null != jsonObject) {
                try {
                    WebAccessToken = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                    LOGGER.info("获取access_token成功!");
                    LOGGER.info("WebAccessToken:{} , openId:{}", WebAccessToken, openId);
                    // 3. 使用获取到的 Access_token 和 openid 拉取用户信息
                    String userMessageUrl = UserInfoUtil.getUserMessage(WebAccessToken, openId);
                    LOGGER.info("第三步:获取用户信息的URL:{}", userMessageUrl);

                    // 通过https方式请求获得用户信息响应
                    String userMessageResponse = HttpsUtil.httpsRequestToString(userMessageUrl, "GET", null);

                    JSONObject userMessageJsonObject = JSON.parseObject(userMessageResponse);
                    LOGGER.info("用户信息:{}", userMessageJsonObject.toJSONString());
                    if (userMessageJsonObject != null) {
                        try {
                            //用户昵称
                            nickName = userMessageJsonObject.getString("nickname");
                            //用户性别
                            sex = userMessageJsonObject.getString("sex");
                            sex = (sex.equals("1")) ? "男" : "女";
                            //用户唯一标识
                            openid = userMessageJsonObject.getString("openid");
                            String headImgUrl = userMessageJsonObject.getString("headimgurl");
                            String province = userMessageJsonObject.getString("province");
                            String city = userMessageJsonObject.getString("city");

                            LOGGER.info("用户昵称:{}", nickName);
                            LOGGER.info("用户性别:{}", sex);
                            LOGGER.info("OpenId:{}", openid);

                            UserRepository userRepository = ApplicationSupport.getBean(UserRepository.class);
                            User user = userRepository.findOneByOpenId(openId);
                            if (user == null) {
                                user = new User();
                            }
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
                        } catch (JSONException e) {
                            LOGGER.error("获取用户信息失败 failed");
                            return "failed!";
                        }
                    }
                } catch (JSONException e) {
                    LOGGER.error("获取Web Access Token失败");
                    return "failed!";
                }
            }
            LOGGER.info("授权成功!");
            String jwtToken = WebUtil.createTokenByOpenId(openId);
            return "redirect:index.html";
//            return "redirect:http://www.dsunyun.com:81/drink?openId="+openId+"&access_token="+WebAccessToken+"&jwtToken="+jwtToken;
        }
        LOGGER.info("end 尚未授权,即将调到微信的授权页面");
        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6aef1915818229a5&redirect_uri=http%3A%2F%2Fwww.dsunyun.com%2Fdrink&response_type=code&scope=snsapi_userinfo&#wechat_redirect";
    }*/

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WxpayNotifyRepository wxpayNotifyRepository;
    /**
     * 支付回调地址
     */
    @RequestMapping(value = "/notify")
    public String wxpaySucc(HttpServletRequest request) throws IOException, JDOMException {
        LOGGER.info("微信支付回调");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> map = PayUtil.doXMLParse(resultxml);
        outSteam.close();
        inStream.close();
        if (!PayUtil.isTenpaySign(map)) {
            LOGGER.info("签名错误！");
            // 支付失败
            return "fail";
        } else {
            LOGGER.info("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------
            Map<String,String> return_data = new HashMap<>();

            if(!"SUCCESS".equals(map.get("return_code"))){
                return_data.put("return_code", "FAIL");
                return_data.put("return_msg", "return_code不正确");
            }else{
                if(!map.get("result_code").toString().equals("SUCCESS")){
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "result_code不正确");
                }
                String out_trade_no = map.get("out_trade_no").toString();
                String time_end = map.get("time_end").toString();
                LOGGER.info("付款完成后，微信系统发送该交易状态通知，交易成功");
                Order order = orderRepository.findOneByOutTradeNo(out_trade_no);
                if(order == null){
                    LOGGER.error("订单不存在");
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "订单不存在");
                    return WXRequestUtil.GetMapToXML(return_data);
                }else {
                    if (order.getPayStatus().equals(1)){
                        LOGGER.info("订单已经支付");
                        return_data.put("return_code", "SUCCESS");
                        return_data.put("return_msg", "OK");
                        return WXRequestUtil.GetMapToXML(return_data);
                    }else {
                        LOGGER.info("订单状态为0代表：尚未支付,1代表支付成功，当前值为:{}",order.getPayStatus());
                        order.setPayStatus(1);
                        WxpayNotify wxpayNotify = order.getWxpayNotify();
                        wxpayNotify.setTimeEnd(map.get("time_end"));
                        LOGGER.info("订单状态为0代表：尚未支付,1代表支付成功，当前值为:{}",order.getPayStatus());
                        orderRepository.save(order);
                        wxpayNotifyRepository.save(wxpayNotify);
                        return_data.put("return_code", "SUCCESS");
                        return_data.put("return_msg", "OK");
                        return WXRequestUtil.GetMapToXML(return_data);
                    }
                }
            }
            return "success";
        }
    }


   /* public void weixin_notify(HttpServletRequest request,HttpServletResponse response) throws Exception{
        //读取参数
        InputStream inputStream ;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s ;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null){
            sb.append(s);
        }
        in.close();
        inputStream.close();

        //解析xml成map
        SortedMap<String, String> m = new TreeMap<>();
        m = XMLUtil.doXMLParseStr(sb.toString());

        //过滤空 设置 TreeMap
        Map<String,String> packageParams = new TreeMap<String,String>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);
            String v = "";
            if(null != parameterValue) {
                packageParams.put(parameter, v);
            }
        }
        // 账号信息
        String key = PayUtil.API_KEY; // key
        LOGGER.info("packageParams:{}",packageParams);
        //判断签名是否正确
        if(PayUtil.isTenpaySign(packageParams)) {
            //------------------------------
            //处理业务开始
            //------------------------------
            String resXml = "";
            if("SUCCESS".equals((String)packageParams.get("result_code"))){
                // 这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String)packageParams.get("mch_id");
                String openid = (String)packageParams.get("openid");
                String is_subscribe = (String)packageParams.get("is_subscribe");
                String out_trade_no = (String)packageParams.get("out_trade_no");
                String total_fee = (String)packageParams.get("total_fee");

                LOGGER.info("mch_id:"+mch_id);
                LOGGER.info("openid:"+openid);
                LOGGER.info("is_subscribe:"+is_subscribe);
                LOGGER.info("out_trade_no:"+out_trade_no);
                LOGGER.info("total_fee:"+total_fee);

                //////////执行自己的业务逻辑////////////////

                LOGGER.info("支付成功");
                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

            } else {
                LOGGER.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            //------------------------------
            //处理业务完毕
            //------------------------------
            BufferedOutputStream out = new BufferedOutputStream(
                    response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        } else{
            LOGGER.info("通知签名验证失败");
        }

    }*/
    @RequestMapping("/get/token")
    @ResponseBody
    public CustomResponse getJwtToken(@RequestParam(name = "openId", required = false) String openId) {
        User user = userRepository.findOneByOpenId(openId);
        Map<String, Object> result = new HashMap<>();
        result.put("jwtToken", user.getJwtToken());
        return success(result);
    }

}

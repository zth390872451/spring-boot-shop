package com.svlada.common.utils.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.svlada.component.WeixinThread;
import com.svlada.config.WxConfig;
import com.svlada.endpoint.wechat.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

import static com.svlada.config.WxConfig.ACCESS_TOKEN;

public class WxCommonUtil {
	private static final Logger log = LoggerFactory.getLogger(WxCommonUtil.class);

	public static String getUuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 签名工具
	 */
	public static String createSign(String characterEncoding, Map<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, Object>> it = parameters.entrySet().iterator();
		while (it.hasNext()) {
			Entry <String,Object>entry = (Entry<String,Object>) it.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();//去掉带sign的项
			if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
				sb.append(key + "=" + value + "&");
			}
		}
		sb.append("key=" + WxConfig.MCH_KEY);
		log.info("before sign  = " + sb.toString());
		//注意sign转为大写
		return MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
	}

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     * @param responseStr API返回的XML数据字符串
     */
    public static boolean checkIsSignValidFromResponseString(String responseStr) {
        try {
        	SortedMap<String, Object> map = XMLUtil.doXMLParse(responseStr);
            //Map<String, Object> map = XMLParser.getMapFromXML(responseString);
            log.debug("xml to map object:{}", map.toString());
            String signFromAPIResponse = map.get("sign").toString();
            if ("".equals(signFromAPIResponse) || signFromAPIResponse == null) {
                log.debug("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
                return false;
            }
            //log.debug("服务器回包里面的签名是:" + signFromAPIResponse);
            //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
            map.put("sign", "");
            //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
            String signForAPIResponse = WxCommonUtil.createSign("UTF-8", map);
            if (!signForAPIResponse.equals(signFromAPIResponse)) {
                //签名验不过，表示这个API返回的数据有可能已经被篡改了
                log.debug("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
                return false;
            }
            log.debug("wxpay 统一下单回调数据验签通过");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
	 * 返回给微信的参数
	 */
	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	/**
	 * 将请求参数转换为xml格式的string
	 */
	public static String getRequestXml(SortedMap<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Iterator<Entry<String, Object>> iterator = parameters.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String,Object> entry = (Entry<String,Object>) iterator.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key)
					|| "sign".equalsIgnoreCase(key)) {
				sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
			} else {
				sb.append("<" + key + ">" + value + "</" + key + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}


	public static String buildConfigSign(){
		String jsapi_ticket = getTicket();//获取ticket
		String sortString = WxCommonUtil.sort(jsapi_ticket, TimeUtil.getTimeStamp(), WxCommonUtil.getUuid());
		String sha1 = WxCommonUtil.sha1(sortString);
		return sha1;
	}

	public static String getTicket(){
		if (StringUtils.isEmpty(ACCESS_TOKEN)){
			String response = HttpsUtil.httpsRequestToString(WxConfig.ACCESS_TOKEN_URL, "GET", null);
			JSONObject jsonObject = JSON.parseObject(response);
			String access_token = jsonObject.getString("access_token");
			ACCESS_TOKEN = access_token;
		}
		String accessToken = getAccessToken();
		String ticketUrl = WxConfig.TICKET_URL.replace("ACCESS_TOKEN",accessToken);
		String response = HttpsUtil.httpsRequestToString(ticketUrl, "GET", null);
		JSONObject jsonObject = JSON.parseObject(response);
		log.info("请求到的 TICKET:{}", jsonObject.toJSONString());
		String ticket = jsonObject.getString("ticket");
		return ticket;
	}


	public static String getAccessToken(){
		if (StringUtils.isEmpty(ACCESS_TOKEN)){
			String response = HttpsUtil.httpsRequestToString(WxConfig.ACCESS_TOKEN_URL, "GET", null);
			JSONObject jsonObject = JSON.parseObject(response);
			String access_token = jsonObject.getString("access_token");
			String expiresIn = jsonObject.getString("expires_in");
			if (!StringUtils.isEmpty(access_token)){
				if (WeixinThread.accessToken==null){
					WeixinThread.accessToken = new AccessToken();
					WeixinThread.accessToken.setToken(access_token);
					WeixinThread.accessToken.setExpiresIn(Integer.valueOf(expiresIn));
				}
			}
			ACCESS_TOKEN = access_token;
		}
		return  ACCESS_TOKEN;
	}



	/**
	 * 排序方法
	 */
	public static String sort(String token, String timestamp, String nonce) {
		String[] strArray = {token, timestamp, nonce};
		Arrays.sort(strArray);
		StringBuilder sb = new StringBuilder();
		for (String str : strArray) {
			sb.append(str);
		}

		return sb.toString();
	}

	/**
	 * 将字符串进行sha1加密
	 *
	 * @param str 需要加密的字符串
	 * @return 加密后的内容
	 */
	public static String sha1(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();
// Create Hex String
			StringBuffer hexString = new StringBuffer();
// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}


	/**
	 * @Description: 前端jssdk页面配置需要用到的配置参数
	 * @param @return hashmap {appid,timestamp,nonceStr,signature}
	 * @param @throws Exception
	 * @author dapengniao
	 * @date 2016年3月19日 下午3:53:23
	 */
	public static HashMap<String, String> jsSDK_Sign(String url,Boolean debug) throws Exception {
		String nonce_str = create_nonce_str();
		String timestamp= TimeUtil.getTimeStamp();
//		String jsapi_ticket= WxCommonUtil.getTicket();
		String jsapi_ticket = WeixinThread.jsapiTicket.getTicket();
		// 注意这里参数名必须全部小写，且必须有序
		String  string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
				+ "&timestamp=" + timestamp  + "&url=" + url;
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(string1.getBytes("UTF-8"));
		String signature = byteToHex(crypt.digest());
		HashMap<String, String> jssdk=new HashMap<String, String>();
		jssdk.put("appId", WxConfig.APP_ID);
		jssdk.put("timestamp", timestamp);
		jssdk.put("nonceStr", nonce_str);
		jssdk.put("signature", signature);
		if (debug){
			jssdk.put("jsapi_ticket", jsapi_ticket);
		}
		return jssdk;

	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}


}

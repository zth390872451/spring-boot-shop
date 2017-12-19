package com.svlada.common.utils.wx;

import com.alibaba.fastjson.JSONObject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class PayUtil {
    //微信参数配置
    public static String API_KEY = "KWgxgxlPrx1Nbg4lhs1LXejItiYSUpwi";
    public static String APPID = "wx003eb60bfed36d47";
    public static String MCH_ID = "1491921032";

    public static String NOTIFY_URL = "http://www.pcjshe.com/callback/wx";


    //随机字符串生成
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //请求xml组装
    public static String getRequestXml(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
//            String value = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key) || "sign".equalsIgnoreCase(key)) {
                sb.append("<" + key + ">" + "<![CDATA[" + entry.getValue() + "]]></" + key + ">");
            } else {
                sb.append("<" + key + ">" + entry.getValue() + "</" + key + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    //生成签名
    public static String createSign(String characterEncoding, SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
//            Object v = entry.getValue();
            if (null != entry.getValue() && !"".equals(entry.getValue())
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + entry.getValue() + "&");
            }
        }
        sb.append("key=" + API_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    //请求方法
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}" + ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}" + e);
        }
        return null;
    }

    //xml解析
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }

    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    public static Logger LOGGER = LoggerFactory.getLogger(PayUtil.class);

    public static Map<String, String> weixinPrePay(String sn, String totalAmount,
                                                   String description, String openId, String ip) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", PayUtil.APPID);
        parameterMap.put("mch_id", PayUtil.MCH_ID);
        parameterMap.put("nonce_str", PayUtil.getRandomString(32));
        parameterMap.put("body", description);
        parameterMap.put("out_trade_no", sn);
        parameterMap.put("fee_type", "CNY");
        parameterMap.put("total_fee", Integer.valueOf(totalAmount));
        parameterMap.put("spbill_create_ip", ip);
        parameterMap.put("notify_url", NOTIFY_URL);
        parameterMap.put("trade_type", "JSAPI");
        parameterMap.put("openid", openId);
        String sign = WxCommonUtil.createSign("UTF-8", parameterMap);
//        String sign = PayUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);
        String requestXML = PayUtil.getRequestXml(parameterMap);
        LOGGER.info("统一下单的 请求参数为：" + requestXML);
        String result = PayUtil.httpsRequest(
                "https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",
                requestXML);
        LOGGER.info("统一下单的 结果为：" + requestXML);
        Map<String, String> map = null;
        try {
            map = PayUtil.doXMLParse(result);
            LOGGER.info("统一下单的 结果map为：" + map);
        } catch (JDOMException e) {
            LOGGER.info("获取预支付订单结果 JDOMException ：{}", e);
        } catch (IOException e) {
            LOGGER.info("获取预支付订单结果 IOException：{}", e);
        }
        return map;
    }

    public static Map<String, Object> createSignAgain(String sn, String totalAmount,
                                                      String description, String openId, String ip) {
        Map<String, String> map = weixinPrePay(sn, totalAmount, description, openId, ip);
        LOGGER.info("获取预支付结果：{}", map);
        JSONObject jsonObject = new JSONObject();
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", PayUtil.APPID);
        parameterMap.put("partnerid", PayUtil.MCH_ID);
        parameterMap.put("prepayid", map.get("prepay_id"));
        parameterMap.put("noncestr", PayUtil.getRandomString(32));
        parameterMap.put("timestamp", TimeUtil.getTimeStamp());
        String sign = PayUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);
        parameterMap.put("package", "prepay_id=" + map.get("prepay_id"));
        jsonObject.put("parameterMap", parameterMap);
        return parameterMap;
    }


    public static boolean isTenpaySign(Map<String, String> map) {
        String charset = "utf-8";
        String signFromAPIResponse = map.get("sign");
        if (signFromAPIResponse == null || signFromAPIResponse.equals("")) {
            LOGGER.info("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }
        LOGGER.info("服务器回包里面的签名是:" + signFromAPIResponse);
//过滤空 设置 TreeMap
        SortedMap<String, String> packageParams = new TreeMap<>();
        for (String parameter : map.keySet()) {
            String parameterValue = map.get(parameter);
            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }

        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);

//将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        //算出签名
        String resultSign = "";
        String tobesign = sb.toString();
        if (null == charset || "".equals(charset)) {
            resultSign = MD5Util.MD5Encode(tobesign, "UTF-8").toUpperCase();
        }
        String tenpaySign = ((String) packageParams.get("sign")).toUpperCase();
        return tenpaySign.equals(resultSign);
    }

    public static void main(String[] args) {
//        Map<String, Object> signAgain = createSignAgain(PayUtil.getRandomString(32), "1", "des", "oKMbG0VaP3KFqnomKKQAh8YDqnBU", "14.153.126.29");
//        System.out.println("signAgain = " + signAgain);
        Map<String, String> map = weixinPrePay("20171216005131329008", "1", "description", "oKMbG0VaP3KFqnomKKQAh8YDqnBU", "14.153.126.29");
        SortedMap<String, Object> finalpackage = new TreeMap<String, Object>();
        finalpackage.put("appId", PayUtil.APPID);
        finalpackage.put("timeStamp", TimeUtil.getTimeStamp());
        finalpackage.put("nonceStr", PayUtil.getRandomString(32));
        finalpackage.put("package", "prepay_id=" + map.get("prepay_id"));
        finalpackage.put("signType", "MD5");
        String sign = PayUtil.createSign("UTF-8", finalpackage);
        finalpackage.put("paySign", sign);
        System.out.println("finalpackage = " + finalpackage);
    }
}
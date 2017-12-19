package com.svlada.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xmc
 * 生成订单号，单机唯一
 */
public class OrderUtil {  
    /** 
     * 锁对象，可以为任意对象 
     */  
    private static Object lockObj = "lockerOrder";  
    /** 
     * 计数器 
     */  
    private static long orderNumCount = 0L;  
    /** 
     * 每毫秒生成订单号数量最大值 
     */  
    private static int maxPerMSECSize=1000;  
    
    public static String genOrderCode() {
    	// 最终生成的订单号  
        String finOrderNum = "";  
        try {  
            synchronized (lockObj) {  
                // 取系统当前时间作为订单号变量前半部分，精确到毫秒  
                long nowLong = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));  
                // 计数器到最大值归零
                if (orderNumCount > maxPerMSECSize) {  
                    orderNumCount = 0L;  
                }  
                //组装订单号  
                String countStr = maxPerMSECSize + orderNumCount + "";  
                finOrderNum = nowLong + countStr.substring(1);  
                orderNumCount++;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return finOrderNum;
    }  
  
}  
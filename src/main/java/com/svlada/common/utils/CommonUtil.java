package com.svlada.common.utils;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {

	//获取客户端ip地址
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	//本地imei号与jasper中的imei号前14位比较
	public static boolean isImeiEq(String sourceImei,String targetImei){
		if (sourceImei.length() < 14 || targetImei.length() < 14) {
			return false;
		}
		if (!sourceImei.substring(0, 14).equals(targetImei.subSequence(0, 14))) {
			return false;
		}
		return true;
	}

}

package com.svlada.common.utils.wx;

import java.util.*;

public class MapUtils {

	/**
	 * 对map根据key进行排序 ASCII 顺序
	 */
	public static SortedMap<String, Object> sortMap(Map<String, Object> map) {
		List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
		// 排序前
		/*
		 * for (int i = 0; i < infoIds.size(); i++) {
		 * System.out.println(infoIds.get(i).toString()); }
		 */

		// 排序
		Collections.sort(infoIds, (o1, o2) -> {
            // return (o2.getValue() - o1.getValue());//value处理
            return (o1.getKey()).toString().compareTo(o2.getKey());
        });
		// 排序后
		SortedMap<String, Object> sortmap = new TreeMap<String, Object>();
		for (int i = 0; i < infoIds.size(); i++) {
			String[] split = infoIds.get(i).toString().split("=");
			sortmap.put(split[0], split[1]);
		}
		return sortmap;
	}

}

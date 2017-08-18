package com.sysconst;

import java.util.HashMap;
import java.util.Map;

public class Function {
	/*
	 * 将对象按json字符串输出
	 */
	/*public static String objToJson(int code,String desc,Object obj) {
		String result = "";
		try {
			JsonQhtGame jsonObj = new JsonQhtGame();
			jsonObj.setCode(code);
			jsonObj.setDesc(desc);
			jsonObj.setInfo(obj);
			result = JSONObject.fromObject(jsonObj).toString();
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
		
		return result;
	}*/
	
	public static Map<String,Object> objToJson(int code,String desc,Object obj) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result.put("code", code);
			result.put("desc", desc);
			result.put("info",obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}

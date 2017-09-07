package com.fm.util;

import com.fasterxml.jackson.databind.ObjectMapper;



public class JsonUtil {
	
	private static ObjectMapper map;
	
	static{
		map = new ObjectMapper();
	}

	public static byte[] toJson(Object r) throws Exception {
		return map.writeValueAsString(r).getBytes();
	}
	
	public static <T> T toObject(String json, Class<T> c) throws Exception{
		return  map.readValue(json, c);
	}

}

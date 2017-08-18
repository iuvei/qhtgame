package com.util;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

public class Dao {
	private static SqlSessionTemplate handler;
	
	public static void init(SqlSessionTemplate _handler) {
		handler = _handler;
	}
	
	public static int insert(String statement, Map<String, Object> parameter) {
		return handler.insert(statement, parameter);
	}
	
	public static int update(String statement, Map<String, Object> parameter) {
		return handler.update(statement, parameter);
	}
	
	public static int delete(String statement, Map<String, Object> parameter) {
		return handler.delete(statement, parameter);
	}
	
	public static Object selectOne(String statement, Map<String, Object> parameter) {
		return handler.selectOne(statement, parameter);
	}
	
	public static Object selectList(String statement, Map<String, Object> parameter) {
		return handler.selectList(statement, parameter);
	}
}

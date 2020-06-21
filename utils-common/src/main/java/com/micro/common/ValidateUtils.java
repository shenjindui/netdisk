package com.micro.common;

/**
 * 验证类
 * @author zwy
 * 2018年11月6日
 */
public class ValidateUtils {
	public static void validate(String name,String msg){
		if(name==null||"".equals(name)||"null".equals(name)){
			throw new RuntimeException(msg+"不能为空");
		}
	}
	public static void validate(Long name,String msg){
		if(name==null||"".equals(name)||"null".equals(name)){
			throw new RuntimeException(msg+"不能为空");
		}
	}
	
	public static void validate(Integer name,String msg){
		if(name==null){
			throw new RuntimeException(msg+"不能为空");
		}
	}
}

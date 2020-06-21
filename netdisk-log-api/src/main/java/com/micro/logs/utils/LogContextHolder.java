package com.micro.logs.utils;

import com.micro.logs.bean.LogInfo;

public class LogContextHolder {
	private static final ThreadLocal<LogInfo> logThtreadLocal = new ThreadLocal<LogInfo>();
	
	public static void set(LogInfo bean){
		logThtreadLocal.set(bean);
	}
	
	public static LogInfo get(){
		return logThtreadLocal.get();
	}
	
	public static void clear(){
		logThtreadLocal.remove();
	}
	
	public static void setRemark(String remark){
		logThtreadLocal.get().setRemark(remark);
    }
}

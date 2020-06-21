package com.micro.chain.core;

public class ContextHolder {
	//因为使用Spring容器管理对象，默认是单例，因此使用ThreadLocal来管理
	private static ThreadLocal<ContextRequest> tlReq=new ThreadLocal<>();
	private static ThreadLocal<ContextResponse> tlRes=new ThreadLocal<>();
	
	public static void setReq(ContextRequest req){
		tlReq.set(req);
	}
	public static ContextRequest getReq(){
		return tlReq.get();
	}
	public static void clearReq(){
		tlReq.remove();
	}
	
	public static void setRes(ContextResponse res){
		tlRes.set(res);
	}
	public static ContextResponse getRes(){
		return tlRes.get();
	}
	public static void clearRes(){
		tlRes.remove();
	}
}

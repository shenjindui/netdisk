package com.micro.chain.core;

public class Bootstrap {
	private HandlerInitializer handlerInitializer;
	
	public void childHandler(HandlerInitializer handlerInitializer){
		this.handlerInitializer=handlerInitializer;
		handlerInitializer.initChannel(handlerInitializer);
	}
	
	public ContextResponse execute(){
		//执行责任链
		execHandler(handlerInitializer.firstHandler, ContextHolder.getReq(), ContextHolder.getRes());
		
		//获取响应结果
		ContextResponse res= ContextHolder.getRes();
		
		//清空
		ContextHolder.clearReq();
		ContextHolder.clearRes();
		return res;
	}
	
	private void execHandler(Handler handler,ContextRequest request,ContextResponse response){
		//执行业务方法
		handler.doHandler(request,response);
		
		//递归执行（下一个节点）
		if(handler.nextHandler!=null){
			execHandler(handler.nextHandler,ContextHolder.getReq(),ContextHolder.getRes());
		}
	}
}

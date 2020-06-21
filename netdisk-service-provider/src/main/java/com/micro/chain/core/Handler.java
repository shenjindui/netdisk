package com.micro.chain.core;

public abstract class Handler {
	public Handler nextHandler;
	public void setNextHandler(Handler nextHandler) {
		this.nextHandler = nextHandler;
	}
	public void updateResponse(ContextResponse response){
		ContextHolder.clearRes();
		ContextHolder.setRes(response);
	}
	public void updateRequest(ContextRequest request){
		ContextHolder.clearReq();
		ContextHolder.setReq(request);
	}
	public abstract void doHandler(ContextRequest request,ContextResponse response);
}

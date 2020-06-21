package com.micro.chain.core;

public abstract class HandlerInitializer extends PipelineImpl{
	public HandlerInitializer(ContextRequest request,ContextResponse response) {
		ContextHolder.setReq(request);
		ContextHolder.setRes(response);
	}

	protected abstract void initChannel(Pipeline pipeline);
}

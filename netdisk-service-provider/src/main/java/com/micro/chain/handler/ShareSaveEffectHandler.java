package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareSaveRequest;
import com.micro.disk.service.ShareService;

@Component
public class ShareSaveEffectHandler extends Handler{
	@Autowired
	private ShareService shareService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSaveRequest){
			ShareSaveRequest bean=(ShareSaveRequest) request;
			
			shareService.validateShareIsEffect(bean.getShareid());
		}else{
			throw new RuntimeException("ShareSaveEffectHandler==参数不对");
		}
	}
}

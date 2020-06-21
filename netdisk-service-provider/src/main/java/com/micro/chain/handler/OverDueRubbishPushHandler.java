package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.OverDueRubbishRequest;
import com.micro.websocket.PushService;

@Component
public class OverDueRubbishPushHandler extends Handler{
	@Autowired
	private PushService pushService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof OverDueRubbishRequest){
			OverDueRubbishRequest bean=(OverDueRubbishRequest) request;
			
			if(bean.getDfd()!=null){
				pushService.pushNotice(bean.getDfd().getCreateuserid());
			}
		}else{
			throw new RuntimeException("OverDueRubbishPushHandler==参数不对");
		}
	}
}

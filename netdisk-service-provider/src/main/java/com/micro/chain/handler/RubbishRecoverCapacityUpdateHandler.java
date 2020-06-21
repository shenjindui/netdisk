package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RubbishRecoverRequest;
import com.micro.disk.service.UserCapacityService;
import com.micro.websocket.PushService;

@Component
public class RubbishRecoverCapacityUpdateHandler extends Handler{
	@Autowired
	private UserCapacityService userCapacityService;
	@Autowired
	private PushService pushService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof RubbishRecoverRequest){
			RubbishRecoverRequest bean=(RubbishRecoverRequest) request;
			
			userCapacityService.updateUserCapacity(0, bean.getCapacity(), bean.getUserid(), bean.getUsername(), "回收站还原");
			
			pushService.pushCapacity(bean.getUserid());
		}else{
			throw new RuntimeException("RubbishRecoverCapacityUpdateHandler==参数不对");
		}
	}
}

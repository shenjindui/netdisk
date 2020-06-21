package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.CreateRequest;
import com.micro.disk.service.UserCapacityService;
import com.micro.websocket.PushService;

@Component
public class CreateCapacityUpdateHandler extends Handler{
	@Autowired
	private UserCapacityService userCapacityService;
	@Autowired
	private PushService pushService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof CreateRequest){
			CreateRequest bean=(CreateRequest) request;
			
			userCapacityService.updateUserCapacity(0, bean.getBytes().length, bean.getUserid(), bean.getUsername(), "创建文档");
			pushService.pushCapacity(bean.getUserid());
		}else{
			throw new RuntimeException("CreateCapacityUpdateHandler==参数不对");
		}
	}

}

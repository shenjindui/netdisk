package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.EditRequest;
import com.micro.disk.service.UserCapacityService;
import com.micro.websocket.PushService;

@Component
public class DiskCapacityUpdateHandler extends Handler{
	@Autowired
	private UserCapacityService userCapacityService;
	@Autowired
	private PushService pushService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof EditRequest){
			EditRequest bean=(EditRequest) request;
			
			userCapacityService.updateUserCapacity(bean.getType(), bean.getSize(), bean.getUserid(), bean.getUsername(), "编辑文档");
			pushService.pushCapacity(bean.getUserid());
		}else{
			throw new RuntimeException("DiskCapacityUpdateHandler==参数不对");
		}
	}
}

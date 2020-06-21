package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareSaveRequest;
import com.micro.disk.service.UserCapacityService;
import com.micro.websocket.PushService;

@Component
public class ShareSaveCapacityUpdateHandler extends Handler{
	@Autowired
	private UserCapacityService userCapacityService;
	@Autowired
	private PushService pushService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSaveRequest){
			ShareSaveRequest bean=(ShareSaveRequest) request;
			
			userCapacityService.updateUserCapacity(0, bean.getCapacity(), bean.getUserid(), bean.getUsername(),"分享文件保存");
			pushService.pushCapacity(bean.getUserid());
		}else{
			throw new RuntimeException("ShareSaveCapacityUpdateHandler==参数不对");
		}
	}
}

package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MergeRequest;
import com.micro.disk.service.UserCapacityService;
import com.micro.websocket.PushService;

@Component
public class MergeCapacityUpdateHandler extends Handler{
	@Autowired
	private UserCapacityService userCapacityService;
	@Autowired
	private PushService pushService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MergeRequest){
			MergeRequest bean=(MergeRequest) request;
			
			userCapacityService.updateUserCapacity(0, bean.getTotalSize(), bean.getUserid(), bean.getUsername(), "文件上传");
			
			pushService.pushCapacity(bean.getUserid());
		}else{
			throw new RuntimeException("MergeCapacityUpdateHandler==参数不对");
		}				
	}

}

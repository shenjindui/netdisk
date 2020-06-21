package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.FileDelRequest;
import com.micro.disk.service.UserCapacityService;
import com.micro.websocket.PushService;

@Component
public class FileDelCapacityHandler extends Handler {
	@Autowired
	private UserCapacityService userCapacityService;
	@Autowired
	private PushService pushService;

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof FileDelRequest){
			FileDelRequest bean=(FileDelRequest) request;
			
			userCapacityService.updateUserCapacity(1, bean.getCapacity(), bean.getCreateuserid(), bean.getCreateusername(), "文件删除");
			
			pushService.pushCapacity(bean.getCreateuserid());
		}else{
			throw new RuntimeException("FileDelCapacityHandler==参数不对");
		}
	}

}

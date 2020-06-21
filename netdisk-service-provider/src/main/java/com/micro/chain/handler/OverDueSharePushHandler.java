package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.OverDueShareRequest;
import com.micro.websocket.PushService;

@Component
public class OverDueSharePushHandler extends Handler{
	@Autowired
	private PushService pushService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof OverDueShareRequest){
			OverDueShareRequest bean=(OverDueShareRequest) request;
			
			if(bean.getDiskShare()!=null){		
				if(bean.getDiskShare().getStatus()==0){					
					pushService.pushNotice(bean.getDiskShare().getShareuserid());
				}
			}
		}else{
			throw new RuntimeException("OverDueSharePushHandler==参数不对");
		}
	}
}

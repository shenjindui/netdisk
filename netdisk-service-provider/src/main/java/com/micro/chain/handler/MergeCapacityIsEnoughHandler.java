package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MergeRequest;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.disk.service.UserCapacityService;

@Component
public class MergeCapacityIsEnoughHandler extends Handler{
	@Autowired
	private UserCapacityService userCapacityService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MergeRequest){
			MergeRequest bean=(MergeRequest) request;
			
			UserCapacityBean ucb=userCapacityService.findUserCapacity(bean.getUserid());
			if(bean.getTotalSize()>(ucb.getTotalcapacity()-ucb.getUsedcapacity())){
				throw new RuntimeException("您的存储空间不足,请联系管理员");
			}
			
		}else{
			throw new RuntimeException("MergeCapacityIsEnoughHandler==参数不对");
		}
	}
}

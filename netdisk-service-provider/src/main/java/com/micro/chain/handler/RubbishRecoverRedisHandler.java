package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RubbishRecoverRequest;
import com.micro.common.Contanst;

@Component
public class RubbishRecoverRedisHandler extends Handler{
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof RubbishRecoverRequest){
			RubbishRecoverRequest bean=(RubbishRecoverRequest) request;
			
			for(String id:bean.getRediskeys()){
				stringRedisTemplate.delete(Contanst.RUBBISH+id);					
			}
		}else{
			throw new RuntimeException("RubbishRecoverRedisHandler==参数不对");
		}
	}
}

package com.micro.chain.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RubbishClearRequest;
import com.micro.common.Contanst;

@Component
public class RubbishClearRedisHandler extends Handler{
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof RubbishClearRequest){
			RubbishClearRequest bean=(RubbishClearRequest) request;
			
			List<String> ids=bean.getRediskeys();
			for(String id:ids){
				stringRedisTemplate.delete(Contanst.RUBBISH+id);					
			}
		}else{
			throw new RuntimeException("RubbishClearRedisHandler==参数不对");
		}
	}
}

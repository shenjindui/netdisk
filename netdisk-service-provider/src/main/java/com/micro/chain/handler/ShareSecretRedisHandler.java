package com.micro.chain.handler;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareSecretRequest;
import com.micro.common.Contanst;

@Component
public class ShareSecretRedisHandler extends Handler{
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSecretRequest){
			ShareSecretRequest bean=(ShareSecretRequest) request;
			
			if(bean.getEffect()!=0){
				stringRedisTemplate.opsForValue().set(Contanst.SHARE+bean.getShareid(), bean.getShareid(), bean.getEffect(), TimeUnit.DAYS);
			}
		}else{
			throw new RuntimeException("ShareSecretRedisHandler==参数不对");
		}
	}
}

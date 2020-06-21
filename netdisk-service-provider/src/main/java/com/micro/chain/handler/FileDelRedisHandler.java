package com.micro.chain.handler;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.FileDelRequest;
import com.micro.common.Contanst;

@Component
public class FileDelRedisHandler extends Handler {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof FileDelRequest){
			FileDelRequest bean=(FileDelRequest) request;
			
			if(!CollectionUtils.isEmpty(bean.getRubbishs())){
				for(String id:bean.getRubbishs()){
					stringRedisTemplate.opsForValue().set(Contanst.RUBBISH+id, id,30, TimeUnit.DAYS);
				}
			}
		}else{
			throw new RuntimeException("FileDelRedisHandler==参数不对");
		}
	}
	
}

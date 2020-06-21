package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppMergeRequest;
import com.micro.chain.param.MergeRequest;
import com.micro.common.Contanst;

@Component
public class AppMergeDelRedisHandler extends Handler{
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppMergeRequest){
			AppMergeRequest bean=(AppMergeRequest) request;
			String key=Contanst.PREFIX_CHUNK_TEMP+"-"+bean.getUserid()+"-"+bean.getFilemd5()+"-"+bean.getFilename()+"-*";
			stringRedisTemplate.delete(key);
		}else{
			throw new RuntimeException("AppMergeDelRedisHandler==参数不对");
		}
	}

}

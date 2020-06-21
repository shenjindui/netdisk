package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MergeRequest;
import com.micro.common.Contanst;

@Component
public class MergeDelRedisHandler extends Handler{
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MergeRequest){
			MergeRequest bean=(MergeRequest) request;
			
			String userid=bean.getUserid();
			String uuid=bean.getUuid();
			String fileid=bean.getFileid();
			String filename=bean.getFilename();
			String key=Contanst.PREFIX_CHUNK_TEMP+"-"+userid+"-"+uuid+"-"+fileid+"-"+filename+"-*";
			stringRedisTemplate.delete(key);
		}else{
			throw new RuntimeException("MergeDelRedisHandler==参数不对");
		}		
	}

}

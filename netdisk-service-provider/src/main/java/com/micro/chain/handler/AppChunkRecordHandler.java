package com.micro.chain.handler;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppChunkRequest;
import com.micro.chain.param.ChunkRequest;
import com.micro.common.Contanst;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.config.RedisChunkTemp;

@Component
public class AppChunkRecordHandler extends Handler{
	private JsonUtils jsonUtils=new JsonJackUtils();
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppChunkRequest){
			//强转
			AppChunkRequest bean=(AppChunkRequest) request;
			
			//实体转换
			RedisChunkTemp temp=new RedisChunkTemp();
			temp.setStorepath(bean.getStorepath());
			temp.setChunks(bean.getChunks());
			temp.setCurrentsize(bean.getBytes().length); //当前切块大小
			temp.setChunk(bean.getChunk());		
			//保存到Redis，并且设置key过期
			String key=Contanst.PREFIX_CHUNK_TEMP+"-"+bean.getUserid()+"-"+bean.getFilemd5()+"-"+bean.getFilename()+"-"+bean.getChunk();
			stringRedisTemplate.opsForValue().set(key, jsonUtils.objectToJson(temp), 30, TimeUnit.MINUTES);
		}else{
			throw new RuntimeException("AppChunkRecordHandler==参数不对");
		}		
	}

}

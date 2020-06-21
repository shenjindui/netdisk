package com.micro.chain.handler;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ChunkRequest;
import com.micro.common.Contanst;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.config.RedisChunkTemp;

@Component
public class ChunkRedisHandler extends Handler{
	private JsonUtils jsonUtils=new JsonJackUtils();
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ChunkRequest){
			//强转
			ChunkRequest chunk=(ChunkRequest) request;
			
			//实体转换
			RedisChunkTemp temp=new RedisChunkTemp();
			temp.setUserid(chunk.getUserid());
			temp.setId(chunk.getId());
			temp.setName(chunk.getName());
			temp.setStorepath(chunk.getStorepath());
			temp.setTypecode(chunk.getTypecode());
			temp.setFilesuffix(FilenameUtils.getExtension(chunk.getName()));
			temp.setSize(chunk.getSize());
			temp.setChunks(chunk.getChunks());
			temp.setCurrentsize(chunk.getBytes().length); //当前切块大小
			temp.setChunk(chunk.getChunk());		
			
			//保存到Redis，并且设置key过期
			String key=Contanst.PREFIX_CHUNK_TEMP+"-"+chunk.getUserid()+"-"+chunk.getUuid()+"-"+chunk.getId()+"-"+chunk.getName()+"-"+chunk.getChunk();
			stringRedisTemplate.opsForValue().set(key, jsonUtils.objectToJson(temp), 30, TimeUnit.MINUTES);
		}else{
			throw new RuntimeException("UploadChunkRedisHandler==参数不对");
		}
	}

}

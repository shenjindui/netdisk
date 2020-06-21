package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ChunkRequest;
import com.micro.chain.param.MergeRequest;
import com.micro.common.Contanst;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.config.RedisChunkTemp;

@Component
public class MergeGetChunkHandler extends Handler{
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	private JsonUtils jsonUtils=new JsonJackUtils();
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MergeRequest){
			//强转
			MergeRequest bean=(MergeRequest) request;
			
			String userid=bean.getUserid();
			String uuid=bean.getUuid();
			String fileid=bean.getFileid();
			String filename=bean.getFilename();
			List<RedisChunkTemp> temps=new ArrayList<>();
			
			//key的规则：userid-uuid-fileid-filename-chunk
			String key=Contanst.PREFIX_CHUNK_TEMP+"-"+userid+"-"+uuid+"-"+fileid+"-"+filename+"-*";
			Set<String> keys=stringRedisTemplate.keys(key);
			for(String k:keys){
				String str=stringRedisTemplate.opsForValue().get(k);
				RedisChunkTemp temp=jsonUtils.jsonToPojo(str, RedisChunkTemp.class);
				temps.add(temp);
			}
			
			//写到下一个Handler
			bean.setTemps(temps);
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("UploadMergeGetChunkHandler==参数不对");
		}
	}

}

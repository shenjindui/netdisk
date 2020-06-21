package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppMergeRequest;
import com.micro.chain.param.MergeRequest;
import com.micro.common.Contanst;
import com.micro.common.ValidateUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.config.RedisChunkTemp;

@Component
public class AppMergeGetRecordHandler extends Handler{
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	private JsonUtils jsonUtils=new JsonJackUtils();
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppMergeRequest){
			//强转
			AppMergeRequest bean=(AppMergeRequest) request;
			
			List<RedisChunkTemp> temps=new ArrayList<>();
			
			//key的规则：userid-filemd5-filename-chunk
			String key=Contanst.PREFIX_CHUNK_TEMP+"-"+bean.getUserid()+"-"+bean.getFilemd5()+"-"+bean.getFilename()+"-*";
			Set<String> keys=stringRedisTemplate.keys(key);
			for(String k:keys){
				String str=stringRedisTemplate.opsForValue().get(k);
				RedisChunkTemp temp=jsonUtils.jsonToPojo(str, RedisChunkTemp.class);
				temps.add(temp);
			}
			
			//判断是否秒传
			Boolean secondUpload=bean.getSecondUpload();
			secondUpload=secondUpload==null?false:secondUpload;
			if(!secondUpload){
				//不是秒传
				if(CollectionUtils.isEmpty(temps)){
					//throw new RuntimeException("查询不到对应的切块记录!");
				}
			}
			
			//写到下一个Handler
			bean.setTemps(temps);
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("AppMergeGetRecordHandler==参数不对");
		}
	}

}

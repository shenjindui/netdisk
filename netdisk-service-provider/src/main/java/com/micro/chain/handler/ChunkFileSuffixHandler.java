package com.micro.chain.handler;

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
import com.micro.db.dao.DiskTypeSuffixDao;
import com.micro.model.DiskTypeSuffix;

@Component
public class ChunkFileSuffixHandler extends Handler{
	private JsonUtils jsonUtils=new JsonJackUtils();
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private DiskTypeSuffixDao diskTypeSuffixDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ChunkRequest){
			//强转
			ChunkRequest chunk=(ChunkRequest) request;
			
			//根据文件名称取后缀
			String suffix=FilenameUtils.getExtension(chunk.getName());
			
			//先去Redis查询，如果不存在，再去MySQL查询
			DiskTypeSuffix dts=null;
			Object obj=stringRedisTemplate.opsForValue().get(Contanst.PREFIX_TYPE_SUFFIX+suffix);
			if(obj==null){				
				dts=diskTypeSuffixDao.findBySuffix(suffix);
				if(dts==null){
					throw new RuntimeException("网盘暂时不支持该格式,请联系管理员添加!"+suffix);
				}else{
					stringRedisTemplate.opsForValue().set(Contanst.PREFIX_TYPE_SUFFIX+suffix, jsonUtils.objectToJson(dts));					
				}
			}else{
				dts=jsonUtils.jsonToPojo(obj.toString(), DiskTypeSuffix.class);
			}
			
			//写到下一个
			chunk.setTypecode(dts.getTypecode());
			this.updateRequest(chunk);
		}else{
			throw new RuntimeException("ChunkFileSuffixHandler==参数不对");
		}
	}
}

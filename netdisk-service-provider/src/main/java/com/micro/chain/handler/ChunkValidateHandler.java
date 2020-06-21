package com.micro.chain.handler;

import org.springframework.stereotype.Component;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ChunkRequest;
import com.micro.common.ValidateUtils;


@Component
public class ChunkValidateHandler extends Handler{
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ChunkRequest){
			//强转
			ChunkRequest chunk=(ChunkRequest) request;
			
			//基本校验
			if(chunk.getBytes()==null||chunk.getBytes().length==0){
				throw new RuntimeException("上传的文件为空");
			}
			ValidateUtils.validate(chunk.getUuid(), "uuid");
			ValidateUtils.validate(chunk.getId(), "文件id");
			ValidateUtils.validate(chunk.getName(), "文件名称");
			ValidateUtils.validate(chunk.getSize(), "文件大小");
			ValidateUtils.validate(chunk.getChunk(), "切块序号");
			ValidateUtils.validate(chunk.getChunks(), "切块数量");
			ValidateUtils.validate(chunk.getUserid(), "用户ID");
			ValidateUtils.validate(chunk.getUsername(), "用户姓名");
			
		}else{
			throw new RuntimeException("ChunkValidateHandler==参数不对");
		}
	}
}

package com.micro.chain.handler;

import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppChunkRequest;
import com.micro.chain.param.ChunkRequest;
import com.micro.common.ValidateUtils;

@Component
public class AppChunkValidateHandler extends Handler{

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppChunkRequest){
			//强转
			AppChunkRequest chunk=(AppChunkRequest) request;
			
			//基本校验
			if(chunk.getBytes()==null||chunk.getBytes().length==0){
				throw new RuntimeException("上传的文件为空");
			}
			ValidateUtils.validate(chunk.getAppId(), "appId");
			ValidateUtils.validate(chunk.getFilemd5(), "文件MD5");
			ValidateUtils.validate(chunk.getFilename(), "文件名称");
			ValidateUtils.validate(chunk.getUserid(), "用户ID");
			ValidateUtils.validate(chunk.getChunk(), "切块序号");
			
		}else{
			throw new RuntimeException("AppChunkValidateHandler==参数不对");
		}
	}

}

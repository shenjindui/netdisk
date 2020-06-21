package com.micro.chain.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.FileDelRequest;
import com.micro.common.ValidateUtils;

@Component
public class FileDelValidateHandler extends Handler{
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof FileDelRequest){
			FileDelRequest bean=(FileDelRequest) request;
			
			ValidateUtils.validate(bean.getCreateuserid(),"操作人ID");
			ValidateUtils.validate(bean.getCreateusername(),"操作人姓名");
			if(CollectionUtils.isEmpty(bean.getIds())){
				throw new RuntimeException("请选择需要删除的记录!");				
			}
		}else{
			throw new RuntimeException("FileDelValidateHandler==参数不对");
		}
	}
}

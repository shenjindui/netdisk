package com.micro.chain.handler;

import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.CreateRequest;
import com.micro.common.ValidateUtils;

@Component
public class CreateValidateHandler extends Handler {
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof CreateRequest){
			CreateRequest bean=(CreateRequest) request;
			
			ValidateUtils.validate(bean.getPid(), "所属目录ID");
			ValidateUtils.validate(bean.getFilename(), "文件名称");
			ValidateUtils.validate(bean.getUserid(), "创建人ID");
			ValidateUtils.validate(bean.getUsername(), "创建人姓名");
			
			if(bean.getBytes()==null||bean.getBytes().length==0){
				throw new RuntimeException("文件内容不能为空");
			}
		}else{
			throw new RuntimeException("CreateValidateHandler==参数不对");
		}
	}
}

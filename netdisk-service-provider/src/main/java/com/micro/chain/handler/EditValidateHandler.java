package com.micro.chain.handler;

import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.EditRequest;
import com.micro.common.ValidateUtils;

@Component
public class EditValidateHandler extends Handler {

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof EditRequest){
			EditRequest bean=(EditRequest) request;
			
			ValidateUtils.validate(bean.getFileid(), "文件ID");
			if(bean.getBytes()==null||bean.getBytes().length==0){
				throw new RuntimeException("文件内容不能为空");
			}
		}else{
			throw new RuntimeException("EditValidateHandler==参数不对");
		}
	}

}

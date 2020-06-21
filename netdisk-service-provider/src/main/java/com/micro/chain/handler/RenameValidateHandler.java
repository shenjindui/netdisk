package com.micro.chain.handler;

import org.springframework.stereotype.Component;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RenameRequest;
import com.micro.common.ValidateUtils;

@Component
public class RenameValidateHandler extends Handler{

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if (request instanceof RenameRequest) {
			RenameRequest bean=(RenameRequest) request;
			
			ValidateUtils.validate(bean.getId(), "主键");
			ValidateUtils.validate(bean.getFilename(), "名称");
			ValidateUtils.validate(bean.getUserid(), "用户ID");
			
		}else{
			throw new RuntimeException("RenameValidateHandler==参数不对");
		}
	}

}

package com.micro.chain.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RubbishClearRequest;
import com.micro.common.ValidateUtils;

@Component
public class RubbishClearValidateHandler extends Handler{

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof RubbishClearRequest){
			RubbishClearRequest bean=(RubbishClearRequest) request;
			
			ValidateUtils.validate(bean.getUserid(), "操作人ID");
			if(CollectionUtils.isEmpty(bean.getIds())){
				throw new RuntimeException("请选择清空记录");
			}
		}else{
			throw new RuntimeException("RubbishClearValidateHandler==参数不对");
		}
	}

}

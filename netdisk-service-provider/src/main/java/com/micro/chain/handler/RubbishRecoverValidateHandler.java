package com.micro.chain.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RubbishRecoverRequest;
import com.micro.common.ValidateUtils;

@Component
public class RubbishRecoverValidateHandler extends Handler{

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof RubbishRecoverRequest){
			RubbishRecoverRequest bean=(RubbishRecoverRequest) request;
			
			ValidateUtils.validate(bean.getFolderid(), "还原到文件夹ID");
			ValidateUtils.validate(bean.getUserid(), "用户ID");
			ValidateUtils.validate(bean.getUsername(), "用户姓名");
			if(CollectionUtils.isEmpty(bean.getIds())){
				throw new RuntimeException("请选择还原记录");
			}
		}else{
			throw new RuntimeException("RubbishRecoverValidateHandler==参数不对");
		}
	}

}

package com.micro.chain.handler;

import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppMergeRequest;
import com.micro.chain.param.MergeRequest;
import com.micro.common.ValidateUtils;

@Component
public class AppMergeValidateHandler extends Handler{

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppMergeRequest){
			//强转
			AppMergeRequest bean=(AppMergeRequest) request;
			
			ValidateUtils.validate(bean.getAppId(), "appID");
			ValidateUtils.validate(bean.getFilemd5(), "文件MD5");
			ValidateUtils.validate(bean.getFilename(), "文件名称");
			ValidateUtils.validate(bean.getFilesize(), "文件大小");
			ValidateUtils.validate(bean.getBusinessid(), "buinessID");
			ValidateUtils.validate(bean.getBusinesstype(), "buinessType");
			ValidateUtils.validate(bean.getUserid(), "用户ID");
			ValidateUtils.validate(bean.getUsername(), "用户名称");
		}else{
			throw new RuntimeException("AppMergeValidateHandler==参数不对");
		}
		
	}

}

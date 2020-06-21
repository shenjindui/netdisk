package com.micro.chain.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.CopyRequest;
import com.micro.common.ValidateUtils;

@Component
public class CopyValidateHandler extends Handler{
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof CopyRequest){
			CopyRequest bean=(CopyRequest) request;
			
			if(CollectionUtils.isEmpty(bean.getIds())){
				throw new RuntimeException("请选择需要移动的记录");
			}
			ValidateUtils.validate(bean.getFolderid(), "选择的文件夹");
			ValidateUtils.validate(bean.getUserid(), "用户ID");
			ValidateUtils.validate(bean.getUsername(), "用户姓名");
			
		}else{
			throw new RuntimeException("CopyValidateHandler==参数不对");
		}
	}
}

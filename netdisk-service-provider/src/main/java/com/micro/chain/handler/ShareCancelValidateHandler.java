package com.micro.chain.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareCancelRequest;

@Component
public class ShareCancelValidateHandler extends Handler{

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareCancelRequest){
			ShareCancelRequest bean=(ShareCancelRequest) request;
			
			if(CollectionUtils.isEmpty(bean.getIds())){
				throw new RuntimeException("请选择取消分享的记录");
			}
		}else{
			throw new RuntimeException("ShareCancelValidateHandler==参数不对");
		}
	}

}

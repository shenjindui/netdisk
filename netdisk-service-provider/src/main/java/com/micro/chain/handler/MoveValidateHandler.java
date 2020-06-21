package com.micro.chain.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MoveRequest;
import com.micro.common.ValidateUtils;

@Component
public class MoveValidateHandler extends Handler{
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MoveRequest){
			MoveRequest bean=(MoveRequest) request;
			
			if(CollectionUtils.isEmpty(bean.getIds())){
				throw new RuntimeException("请选择需要移动的记录");
			}
			ValidateUtils.validate(bean.getFolderid(), "选择的文件夹");
			ValidateUtils.validate(bean.getUserid(), "操作人ID");
		}else{
			throw new RuntimeException("MoveValidateHandler==参数不对");
		}
	}
}

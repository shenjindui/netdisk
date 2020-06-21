package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.EditRequest;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.model.DiskMd5;

@Component
public class EditExistHandler extends Handler{
	@Autowired
	private DiskMd5Dao diskMd5Dao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof EditRequest){
			EditRequest bean=(EditRequest) request;
			
			DiskMd5 dm=diskMd5Dao.findMd5IsExist(bean.getFilemd5());
			boolean flag=dm==null?false:true;
			
			//写入下一个Handler
			bean.setExist(flag);
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("EditExistHandler==参数不对");
		}
	}

}

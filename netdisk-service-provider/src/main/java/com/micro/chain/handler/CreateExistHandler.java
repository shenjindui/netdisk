package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.CreateRequest;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.model.DiskMd5;

@Component
public class CreateExistHandler extends Handler{
	@Autowired
	private DiskMd5Dao diskMd5Dao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof CreateRequest){
			CreateRequest bean=(CreateRequest) request;
			
			String filemd5=bean.getFilemd5();			
			DiskMd5 dm=diskMd5Dao.findMd5IsExist(filemd5);
			boolean flag=dm==null?false:true;
			
			//写到下一个Handler
			bean.setExist(flag);
			this.updateRequest(bean);
			
		}else{
			throw new RuntimeException("CreateExistHandler==参数不对");
		}
	}

}

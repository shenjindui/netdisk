package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.OverDueRubbishRequest;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.model.DiskFileDel;

@Component
public class OverDueGetDataHandler extends Handler{
	@Autowired
	private DiskFileDelDao diskFileDelDao;

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof OverDueRubbishRequest){
			OverDueRubbishRequest bean=(OverDueRubbishRequest) request;
			
			DiskFileDel dfd=diskFileDelDao.findOne(bean.getId());
			
			//下一个Handler
			bean.setDfd(dfd);
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("OverDueGetDataHandler==参数不对");
		}
	}

}

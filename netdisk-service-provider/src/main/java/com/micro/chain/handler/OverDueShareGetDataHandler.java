package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.OverDueShareRequest;
import com.micro.db.dao.DiskShareDao;
import com.micro.model.DiskShare;

@Component
public class OverDueShareGetDataHandler extends Handler{
	@Autowired
	private DiskShareDao diskShareDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof OverDueShareRequest){
			OverDueShareRequest bean=(OverDueShareRequest) request;
			
			DiskShare ds=diskShareDao.findOne(bean.getId());
			//下一个Handler
			bean.setDiskShare(ds);
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("OverDueShareGetDataHandler==参数不对");
		}
	}

}

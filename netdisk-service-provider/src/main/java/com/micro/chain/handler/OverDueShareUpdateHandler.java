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
public class OverDueShareUpdateHandler extends Handler{
	@Autowired
	private DiskShareDao diskShareDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof OverDueShareRequest){
			OverDueShareRequest bean=(OverDueShareRequest) request;
			
			DiskShare ds=bean.getDiskShare();
			if(ds!=null){
				if(ds.getStatus()==0){	
					ds.setStatus(1);
					diskShareDao.save(ds);
				}
			}
		}else{
			throw new RuntimeException("OverDueShareGetDataHandler==参数不对");
		}
	}
}

package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RenameRequest;
import com.micro.db.dao.DiskFileDao;

@Component
public class RenameUpdateHandler extends Handler{
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if (request instanceof RenameRequest) {
			RenameRequest bean=(RenameRequest) request;
			
			diskFileDao.rename(bean.getId(), bean.getFilename());
		}else{
			throw new RuntimeException("RenameUpdateHandler==参数不对");
		}
	}

}

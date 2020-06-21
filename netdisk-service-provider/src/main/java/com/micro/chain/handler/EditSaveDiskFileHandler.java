package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.EditRequest;
import com.micro.db.dao.DiskFileDao;

@Component
public class EditSaveDiskFileHandler extends Handler {
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof EditRequest){
			EditRequest bean=(EditRequest) request;

			String filemd5=bean.getFilemd5();
			long filesize=bean.getBytes().length;
			String id=bean.getFileid();
			diskFileDao.updateField(filemd5, filesize, id);
			
		}else{
			throw new RuntimeException("EditSaveDiskFileHandler==参数不对");
		}
	}
}

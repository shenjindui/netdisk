package com.micro.chain.handler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.EditRequest;
import com.micro.db.dao.DiskFileEditDao;
import com.micro.model.DiskFileEdit;

@Component
public class DiskSaveDiskFileEditHandler extends Handler {
	@Autowired
	private DiskFileEditDao diskFileEditDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof EditRequest){
			EditRequest bean=(EditRequest) request;
			
			String filemd5=bean.getFilemd5();
			String prevfilemd5=bean.getPrevfilemd5();
			if(!filemd5.equals(prevfilemd5)){
				
				DiskFileEdit dfe=new DiskFileEdit();
				dfe.setFileid(bean.getFileid());
				dfe.setEdituserid(bean.getUserid());
				dfe.setEditusername(bean.getUsername());
				dfe.setFilemd5(filemd5);
				dfe.setPrevfilemd5(prevfilemd5);
				dfe.setEdittime(new Date());
				diskFileEditDao.save(dfe);
			}
		}else{
			throw new RuntimeException("DiskSaveDiskFileEditHandler==参数不对");
		}
	}
}

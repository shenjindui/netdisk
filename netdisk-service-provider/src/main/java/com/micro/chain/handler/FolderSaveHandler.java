package com.micro.chain.handler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.FolderRequest;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;

@Component
public class FolderSaveHandler extends Handler{
	@Autowired
	private DiskFileDao diskFileDao;

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof FolderRequest){
			FolderRequest bean=(FolderRequest) request;
			
			DiskFile df=new DiskFile();
			df.setFilename(bean.getFilename());
			df.setPid(bean.getPid());
			df.setFiletype(0);
			df.setFilesize(0);
			df.setCreateuserid(bean.getUserid());
			df.setCreateusername(bean.getUsername());
			df.setCreatetime(new Date());
			diskFileDao.save(df);
			
			
			//写入下一个Handler
			bean.setDiskfileid(df.getId());
			this.updateRequest(bean);
			
		}else{
			throw new RuntimeException("FolderSaveHandler==参数不对");
		}		
	}

}

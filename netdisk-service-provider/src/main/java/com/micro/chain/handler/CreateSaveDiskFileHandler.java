package com.micro.chain.handler;

import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.CreateRequest;
import com.micro.chain.param.CreateResponse;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;

@Component
public class CreateSaveDiskFileHandler extends Handler {
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof CreateRequest){
			CreateRequest bean=(CreateRequest) request;
			
			DiskFile df=diskFileDao.findFileNameIsExist(bean.getUserid(), bean.getPid(), bean.getFilemd5(), bean.getFilename());
			String fileid="";
			if(df==null){
				df=new DiskFile();
				df.setPid(bean.getPid());
				df.setFilename(bean.getFilename());
				df.setFilesize(bean.getBytes().length);
				df.setFilesuffix(FilenameUtils.getExtension(bean.getFilename()));
				df.setTypecode("document");
				df.setFilemd5(bean.getFilemd5());
				df.setFiletype(1);
				df.setCreateuserid(bean.getUserid());
				df.setCreateusername(bean.getUsername());
				df.setCreatetime(new Date());
				diskFileDao.save(df);
				
				fileid=df.getId();
			}else{
				fileid=df.getId();
			}
			
			//写到下个Handler
			bean.setFileid(fileid);
			this.updateRequest(bean);
			
			if(response instanceof CreateResponse){
				CreateResponse res=(CreateResponse) response;
				res.setFileid(fileid);
				this.updateResponse(res);
			}
		}else{
			throw new RuntimeException("CreateSaveDiskFileHandler==参数不对");
		}
	}
	
}

package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RenameRequest;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;

@Component
public class RenameIsExistHandler extends Handler{
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if (request instanceof RenameRequest) {
			RenameRequest bean=(RenameRequest) request;
			
			DiskFile df=diskFileDao.findOne(bean.getId());
			if(df==null){
				throw new RuntimeException("主键不存在");
			}
			int count=diskFileDao.findFilenameIsExistEdit(bean.getUserid(),df.getPid(), bean.getId(), bean.getFilename(), df.getFiletype());
			if(count>0){
				throw new RuntimeException("已经存在相同名称!");
			}
			
		}else{
			throw new RuntimeException("RenameIsExistHandler==参数不对");
		}
	}

}

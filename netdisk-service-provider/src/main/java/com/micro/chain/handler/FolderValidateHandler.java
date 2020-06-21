package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.FolderRequest;
import com.micro.common.ValidateUtils;
import com.micro.db.dao.DiskFileDao;

@Component
public class FolderValidateHandler extends Handler{
	@Autowired
	private DiskFileDao diskFileDao;

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof FolderRequest){
			FolderRequest bean=(FolderRequest) request;
			
			//操作校验
			ValidateUtils.validate(bean.getFilename(), "文件夹名称");
			ValidateUtils.validate(bean.getUserid(), "操作人id");
			ValidateUtils.validate(bean.getUsername(), "操作人姓名");
			if(StringUtils.isEmpty(bean.getPid())){
				bean.setPid("0");
			}
			
			//判断是否存在
			Integer count=diskFileDao.findFolderIsExistAdd(bean.getUserid(), bean.getPid(), bean.getFilename());
			if(count>0){
				throw new RuntimeException("该名称已经存在!");
			}
			
			//写到下一个Handler
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("FolderValidateHandler==参数不对");
		}
	}

}

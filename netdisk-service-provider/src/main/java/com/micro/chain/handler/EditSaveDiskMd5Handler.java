package com.micro.chain.handler;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.EditRequest;
import com.micro.common.Contanst;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.model.DiskMd5;

@Component
public class EditSaveDiskMd5Handler extends Handler {
	@Autowired
	private DiskMd5Dao diskMd5Dao;
	
	@NacosValue(value="${chunksize}",autoRefreshed=true)
	private int chunksize;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof EditRequest){
			EditRequest bean=(EditRequest) request;
			
			if(!bean.isExist()){				
				//文件大小
				int len=bean.getBytes().length;
				//切块数量
				int filenum=(len%chunksize)==0?(len/chunksize):(len/chunksize)+1;
				
				DiskMd5 dm=new DiskMd5();
				dm.setMd5(bean.getFilemd5());
				dm.setFilenum(filenum);
				dm.setTypecode("document");
				dm.setFilename(bean.getFilename());
				dm.setFilesuffix(FilenameUtils.getExtension(bean.getFilename()));
				dm.setFilesize(len);
				diskMd5Dao.save(dm);
			}
			
		}else{
			throw new RuntimeException("EditSaveDiskMd5Handler==参数不对");
		}
	}
}

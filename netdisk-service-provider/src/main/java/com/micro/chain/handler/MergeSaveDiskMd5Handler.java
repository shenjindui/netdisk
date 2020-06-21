package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MergeRequest;
import com.micro.config.RedisChunkTemp;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.model.DiskMd5;

@Component
public class MergeSaveDiskMd5Handler extends Handler {
	@Autowired
	private DiskMd5Dao diskMd5Dao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MergeRequest){
			MergeRequest bean=(MergeRequest) request;
			String filesuffix="";
			String typecode="";
			
			if(!bean.isExistindiskmd5()){//在disk_md5中不存在			
				RedisChunkTemp rct=bean.getTemps().get(0);
				DiskMd5 diskMd5=new DiskMd5();
				diskMd5.setMd5(bean.getFilemd5());
				diskMd5.setFilename(bean.getFilename());
				diskMd5.setFilesize(bean.getTotalSize());
				diskMd5.setFilenum(bean.getTemps().size());
				diskMd5.setTypecode(rct.getTypecode());
				diskMd5.setFilesuffix(rct.getFilesuffix());
				diskMd5Dao.save(diskMd5);
				
				filesuffix=rct.getFilesuffix();
				typecode=rct.getTypecode();
				
				//写到下一个Handler
				bean.setFilesuffix(filesuffix);
				bean.setTypecode(typecode);
				this.updateRequest(bean);
			}
			
		}else{
			throw new RuntimeException("MergeSaveDiskMd5Handler==参数不对");
		}		
	}

}

package com.micro.chain.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.EditRequest;
import com.micro.db.dao.DiskMd5ChunkDao;
import com.micro.model.DiskMd5Chunk;
import com.micro.modeltree.FileChunk;

@Component
public class EditSaveDiskMd5ChunkHandler extends Handler {
	@Autowired
	private DiskMd5ChunkDao diskMd5ChunkDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof EditRequest){
			EditRequest bean=(EditRequest) request;
			
			if(!bean.isExist()){
				List<FileChunk> chunks=bean.getChunks();
				String filemd5=bean.getFilemd5();
				String filename=bean.getFilename();
				long totalsize=bean.getBytes().length;
				
				
				if(!CollectionUtils.isEmpty(chunks)){
					for(int i=0;i<chunks.size();i++){
						FileChunk fc=chunks.get(i);
						
						DiskMd5Chunk dmc=new DiskMd5Chunk();
						dmc.setFilemd5(filemd5);
						dmc.setChunkname(filename);
						dmc.setChunknumber(i);
						dmc.setChunksize(fc.getBytes().length);
						dmc.setTotalchunks(chunks.size());
						dmc.setTotalsize(totalsize);
						dmc.setStorepath(fc.getStorepath());
						diskMd5ChunkDao.save(dmc);
					}
				}		
			}
		}else{
			throw new RuntimeException("EditSaveDiskMd5ChunkHandler==参数不对");
		}
	}
}

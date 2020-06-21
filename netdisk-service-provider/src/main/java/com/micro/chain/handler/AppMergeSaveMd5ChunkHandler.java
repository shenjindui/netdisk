package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppMergeRequest;
import com.micro.chain.param.MergeRequest;
import com.micro.config.RedisChunkTemp;
import com.micro.db.dao.DiskMd5ChunkDao;
import com.micro.model.DiskMd5Chunk;

@Component
public class AppMergeSaveMd5ChunkHandler extends Handler{
	@Autowired
	private DiskMd5ChunkDao diskMd5ChunkDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppMergeRequest){
			AppMergeRequest bean=(AppMergeRequest) request;
			
			if(!bean.isMd5isExist()){
				List<RedisChunkTemp> temps=bean.getTemps();
				if(!CollectionUtils.isEmpty(temps)){
					List<DiskMd5Chunk> entities=new ArrayList<>();					
					for(RedisChunkTemp temp:temps){
						DiskMd5Chunk dmc=new DiskMd5Chunk();
						dmc.setFilemd5(bean.getFilemd5());
						dmc.setChunkname(bean.getFilename());
						dmc.setChunknumber(temp.getChunk());
						dmc.setChunksize(temp.getCurrentsize().longValue());
						dmc.setTotalchunks(temp.getChunks());
						dmc.setTotalsize(bean.getFilesize());
						dmc.setStorepath(temp.getStorepath());
						entities.add(dmc);
					}
					diskMd5ChunkDao.save(entities);
				}
			}
		}else{
			throw new RuntimeException("AppMergeSaveMd5ChunkHandler==参数不对");
		}		
	}

}

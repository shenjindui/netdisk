package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ChunkRequest;
import com.micro.store.context.StoreContext;
import com.micro.store.service.StoreService;

@Component
public class ChunkStoreHandler extends Handler{
	@Autowired
	private StoreContext storeContext;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ChunkRequest){			
			//强转
			ChunkRequest chunk=(ChunkRequest) request;
			//上传
			String storepath=storeContext.upload("", chunk.getBytes(), chunk.getName());
			//写到下一个
			chunk.setStorepath(storepath);
			this.updateRequest(chunk);
		}else{
			throw new RuntimeException("UploadChunkStoreHandler==参数不对");
		}
	}
}

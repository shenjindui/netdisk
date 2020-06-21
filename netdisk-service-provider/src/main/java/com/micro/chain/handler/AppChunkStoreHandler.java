package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppChunkRequest;
import com.micro.chain.param.ChunkRequest;
import com.micro.store.context.StoreContext;

@Component
public class AppChunkStoreHandler extends Handler{
	@Autowired
	private StoreContext storeContext;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppChunkRequest){			
			//强转
			AppChunkRequest bean=(AppChunkRequest) request;
			
			//上传
			String storepath=storeContext.upload("", bean.getBytes(),bean.getFilename());
			
			//写到下一个
			bean.setStorepath(storepath);
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("AppChunkStoreHandler==参数不对");
		}		
	}

}

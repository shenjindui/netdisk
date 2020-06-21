package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppMergeRequest;
import com.micro.chain.param.MergeRequest;

@Component
public class AppMergeSpecialDealHandler extends Handler{
	@Autowired
	private FiledealContext filedealContext;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppMergeRequest){
			AppMergeRequest bean=(AppMergeRequest) request;
			
			//filemd5在disk_md5不存在，则需要裁剪（图片、视频）
			if(bean.isMd5isExist()==false){
				filedealContext.deal(bean.getTypecode(),bean.getFilemd5(), bean.getTemps());
			}
		}else{
			throw new RuntimeException("AppMergeSpecialDealHandler==参数不对");
		}				
	}

}

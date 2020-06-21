package com.micro.chain.handler;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppMergeRequest;
import com.micro.chain.param.MergeRequest;
import com.micro.config.RedisChunkTemp;

@Component
public class AppMergeIsFullHandler extends Handler{

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppMergeRequest){
			//强转
			AppMergeRequest bean=(AppMergeRequest) request;
			List<RedisChunkTemp> temps=bean.getTemps();
			
			if(!CollectionUtils.isEmpty(temps)){			
				long size=0l;
				long totalsize=bean.getFilesize();
				for(int i=0;i<temps.size();i++){
					size=size+temps.get(i).getCurrentsize().longValue();
				}
				if(size!=totalsize){
					throw new RuntimeException("文件完整性校验不通过（前端计算大小："+totalsize+";后端计算大小："+size+"）");
				}
			}
		}else{
			throw new RuntimeException("AppMergeIsFullHandler==参数不对");
		}
	}

}

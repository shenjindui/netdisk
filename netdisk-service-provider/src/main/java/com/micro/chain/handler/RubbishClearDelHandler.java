package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RubbishClearRequest;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.modeltree.DiskFileDelTree;

@Component
public class RubbishClearDelHandler extends Handler{
	@Autowired
	private DiskFileDelDao diskFileDelDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof RubbishClearRequest){
			RubbishClearRequest bean=(RubbishClearRequest) request;
			
			List<DiskFileDelTree> trees=bean.getTrees();
			List<String> rediskeys=new ArrayList<>();
			dgClear(trees, rediskeys);
			
			//下一个Handler
			bean.setRediskeys(rediskeys);
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("RubbishClearDelHandler==参数不对");
		}
	}
	public void dgClear(List<DiskFileDelTree> trees,List<String> rediskeys){
		if(!CollectionUtils.isEmpty(trees)){
			for(DiskFileDelTree tree:trees){
				//获取
				rediskeys.add(tree.getId());
				
				//删除
				diskFileDelDao.deleteById(tree.getId());
				
				//递归删除
				dgClear(tree.getChildren(),rediskeys);
			}
		}
	}
}

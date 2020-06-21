package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareCancelRequest;
import com.micro.db.dao.DiskShareDao;
import com.micro.model.DiskShare;

@Component
public class ShareCancelUpdateHandler extends Handler{
	@Autowired
	private DiskShareDao diskShareDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareCancelRequest){
			ShareCancelRequest bean=(ShareCancelRequest) request;
			
			List<String> ids=bean.getIds();
			List<String> secretIds=new ArrayList<>();
			List<String> friendIds=new ArrayList<>();
			
			for(int i=0;i<ids.size();i++){
				String id=ids.get(i);
				int index=i+1;
				
				DiskShare share=diskShareDao.findOne(id);
				if(share==null){
					throw new RuntimeException("ID不存在");
				}
				if(share.getStatus()==1){
					throw new RuntimeException("选中的第"+index+"条记录已经失效,无法取消分享!");
				}
				if(share.getStatus()==2){
					throw new RuntimeException("选择的第"+index+"条记录已取消分享,无法再取消分享!");
				}
				share.setStatus(2);//0正常，1已失效，2已撤销
				diskShareDao.save(share);
				
				if(share.getType()==0){
					secretIds.add(id);
				}else if(share.getType()==1){
					friendIds.add(id);
				}
			}
			
			//下一个Handler
			bean.setSecretIds(secretIds);
			bean.setFriendIds(friendIds);
			this.updateRequest(bean);
			
		}else{
			throw new RuntimeException("ShareCancelUpdateHandler==参数不对");
		}
	}
}

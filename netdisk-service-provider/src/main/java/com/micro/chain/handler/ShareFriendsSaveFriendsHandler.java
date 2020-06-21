package com.micro.chain.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareFriendsRequest;
import com.micro.db.dao.DiskShareFriendsDao;
import com.micro.disk.bean.ShareFriendsBean;
import com.micro.model.DiskShareFriends;

@Component
public class ShareFriendsSaveFriendsHandler extends Handler{
	@Autowired
	private DiskShareFriendsDao diskShareFriendsDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareFriendsRequest){
			ShareFriendsRequest bean=(ShareFriendsRequest) request;
			
			List<ShareFriendsBean> friends=bean.getFriends();
			for(ShareFriendsBean friend:friends){
				DiskShareFriends dsf=new DiskShareFriends();
				dsf.setShareid(bean.getShareid());
				dsf.setUserid(friend.getUserid());
				dsf.setUsername(friend.getUsername());
				diskShareFriendsDao.save(dsf);
			}
		}else{
			throw new RuntimeException("ShareFriendsSaveFriendsHandler==参数不对");
		}
	}
}

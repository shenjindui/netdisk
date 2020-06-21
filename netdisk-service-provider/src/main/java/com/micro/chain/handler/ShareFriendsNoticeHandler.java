package com.micro.chain.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareFriendsRequest;
import com.micro.common.NoticeConstant;
import com.micro.db.dao.DiskNoticeDao;
import com.micro.disk.bean.ShareFriendsBean;
import com.micro.model.DiskNotice;
import com.micro.websocket.PushService;

@Component
public class ShareFriendsNoticeHandler extends Handler{
	@Autowired
	private DiskNoticeDao diskNoticeDao;
	@Autowired
	private PushService pushService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareFriendsRequest){
			ShareFriendsRequest bean=(ShareFriendsRequest) request;
			
			List<ShareFriendsBean> friends=bean.getFriends();
			if(!CollectionUtils.isEmpty(friends)){
				for(ShareFriendsBean m:friends){
					String userid=m.getUserid();
					String username=m.getUsername();
					String content=username+"给您分享了【"+bean.getTitle()+"】文件,请注意查收";
					
					//保存通知
					DiskNotice notice=new DiskNotice();
					notice.setType(NoticeConstant.NOTICE_SHARE_FRIENDS_CREATE);
					notice.setTypename(NoticeConstant.getTypeName(NoticeConstant.NOTICE_SHARE_FRIENDS_CREATE));
					notice.setContent(content);
					notice.setUserid(userid);
					notice.setUsername(username);
					notice.setCreatetime(new Date());
					notice.setStatus(0);
					diskNoticeDao.save(notice);
					
					//推送给好友		
					pushService.pushNotice(userid);
				}
			}
		}else{
			throw new RuntimeException("ShareFriendsNoticeHandler==参数不对");
		}
	}
}

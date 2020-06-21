package com.micro.chain.handler;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareCancelRequest;
import com.micro.common.NoticeConstant;
import com.micro.db.dao.DiskNoticeDao;
import com.micro.db.dao.DiskShareDao;
import com.micro.db.dao.DiskShareFriendsDao;
import com.micro.model.DiskNotice;
import com.micro.model.DiskShare;
import com.micro.model.DiskShareFriends;
import com.micro.websocket.PushService;

@Component
public class ShareCancelFriendHandler extends Handler{
	@Autowired
	private DiskShareFriendsDao diskShareFriendsDao;
	@Autowired
	private DiskShareDao diskShareDao;
	@Autowired
	private DiskNoticeDao diskNoticeDao;
	@Autowired
	private PushService pushService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareCancelRequest){
			ShareCancelRequest bean=(ShareCancelRequest) request;
			
			List<String> ids=bean.getFriendIds();
			ids.forEach(id->{
				DiskShare ds=diskShareDao.findOne(id);
				List<DiskShareFriends> lists=diskShareFriendsDao.findListByShareid(id);
				
				if(!CollectionUtils.isEmpty(lists)){
					for(DiskShareFriends m:lists){
						String userid=m.getUserid();
						String username=m.getUsername();
						String content=ds.getShareusername()+"已经取消了【"+ds.getTitle()+"】文件的分享!";
						
						//保存通知
						DiskNotice notice=new DiskNotice();
						notice.setType(NoticeConstant.NOTICE_SHARE_FRIENDS_CANCEL);
						notice.setTypename(NoticeConstant.getTypeName(NoticeConstant.NOTICE_SHARE_FRIENDS_CANCEL));
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
			});
		}else{
			throw new RuntimeException("ShareCancelFriendHandler==参数不对");
		}
	}
}

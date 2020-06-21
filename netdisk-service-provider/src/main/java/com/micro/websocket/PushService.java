package com.micro.websocket;

import java.util.ArrayList;
import java.util.List;
import javax.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.disk.bean.NoticeBean;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.disk.service.NoticeService;
import com.micro.disk.service.UserCapacityService;

@Component
public class PushService {
	private JsonUtils jsonUtils=new JsonJackUtils();
	@Autowired
	private UserCapacityService userCapacityService;
	@Autowired
	private NoticeService noticeService;
	
	public void pushCapacityOnOpen(String userid,Session session){
		UserCapacityBean capacity=userCapacityService.findUserCapacity(userid);
		Message msg=new Message();
    	msg.setType(0);
    	msg.setData(capacity);
    	session.getAsyncRemote().sendText(jsonUtils.objectToJson(msg));
	}
	public void pushCapacity(String userid){
		UserCapacityBean capacity=userCapacityService.findUserCapacity(userid);
		List<Session> sessions=SessionUtils.getChannels(userid);
		if(!CollectionUtils.isEmpty(sessions)){
			for(Session session:sessions){
				Message msg=new Message();
				msg.setType(0);
				msg.setData(capacity);
				session.getAsyncRemote().sendText(jsonUtils.objectToJson(msg));
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void pushNoticeOnOpen(String userid,Session session){
		List<NoticeBean> notices=noticeService.findNotices(userid);
		Integer count=noticeService.findNoticesCount(userid);
		if(count>0){			
			Message msg=new Message();
			msg.setType(1);
			msg.setData(notices);
			msg.setCount(count);
			session.getAsyncRemote().sendText(jsonUtils.objectToJson(msg));
		}
	}
	
	
	public void pushNotice(String userid){
		List<NoticeBean> notices=noticeService.findNotices(userid);
		Integer count=noticeService.findNoticesCount(userid);
		if(count>0){			
			List<Session> sessions=SessionUtils.getChannels(userid);
			if(!CollectionUtils.isEmpty(sessions)){
				for(Session session:sessions){				
					Message msg=new Message();
					msg.setType(1);
					msg.setData(notices);
					msg.setCount(count);
					session.getAsyncRemote().sendText(jsonUtils.objectToJson(msg));
				}
			}
		}
	}
	/**
	 * 当删除|已读通知的时候，向用户推送空通知
	 * @param userid
	 */
	public void pushEmptyNotice(String userid){
		List<Session> sessions=SessionUtils.getChannels(userid);
		if(!CollectionUtils.isEmpty(sessions)){
			for(Session session:sessions){		
				List<NoticeBean> notices= new ArrayList<NoticeBean>();
				Message msg=new Message();
				msg.setType(1);
				msg.setData(notices);
				msg.setCount(0);
				session.getAsyncRemote().sendText(jsonUtils.objectToJson(msg));
			}
		}
	}
}

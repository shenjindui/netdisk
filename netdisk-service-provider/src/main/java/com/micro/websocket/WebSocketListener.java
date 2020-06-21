package com.micro.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.micro.utils.SpringContentUtils;

@Component
@ServerEndpoint(value="/websocket/{userid}/{token}")
public class WebSocketListener {
	//成功建立连接
	@OnOpen
	public void onOpen(Session session,@PathParam("userid") String userid,@PathParam("token") String token){
		if(StringUtils.isEmpty(userid)||"null".equals(userid)){
			return;
		}
		if(StringUtils.isEmpty(token)||"null".equals(token)){
			return;
		}
		
		PushService pushService=(PushService) SpringContentUtils.applicationContext.getBean("pushService");
		//添加Session
		SessionUtils.addChannel(userid, token, session);
		
		//查询容量和通知
		pushService.pushCapacityOnOpen(userid, session);
		pushService.pushNoticeOnOpen(userid, session);
		
	}
	
	//连接关闭
	@OnClose
	public void onClose(Session session,@PathParam("userid") String userid,@PathParam("token") String token){
		if(StringUtils.isEmpty(userid)||"null".equals(userid)){
			return;
		}
		if(StringUtils.isEmpty(token)||"null".equals(token)){
			return;
		}
    	SessionUtils.removeChannel(userid, token);
	}
	
	//发生错误
	@OnError
	public void onError(Session session,@PathParam("userid") String userid,@PathParam("token") String token,Throwable error){
		if(StringUtils.isEmpty(userid)||"null".equals(userid)){
			return;
		}
		if(StringUtils.isEmpty(token)||"null".equals(token)){
			return;
		}
		
		SessionUtils.removeChannel(userid, token);
	}
}

package com.micro.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

public class SessionUtils {
	public static Map<String,Map<String,Session>> client=new HashMap<String,Map<String,Session>>();
	public static Object obj=new Object();
	/**
	 * 添加连接
	 * @param userid
	 * @param token
	 * @param channel
	 */
	public static void addChannel(String userid,String token,Session channel){
		synchronized (obj) {
			try{				
				Map<String,Session> map=client.get(userid);
				if(map==null){
					map=new HashMap<String,Session>();
					map.put(token, channel);
					client.put(userid, map);
				}else{
					Session socket=map.get(token);
					if(socket!=null){
						socket.close();//断开连接
						map.remove(token);//移除
					}
					map.put(token, channel);
					client.put(userid, map);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/**
	 * 移除连接
	 * @param userid
	 * @param token
	 */
	public static void removeChannel(String userid,String token){
		synchronized (obj) {
			try{				
				Map<String,Session> map=client.get(userid);
				if(map!=null){				
					Session socket=map.get(token);
					if(socket!=null){					
						socket.close();//断开连接
						map.remove(token);//移除
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取连接
	 * @param userid
	 * @return
	 */
	public static List<Session> getChannels(String userid){
		Map<String,Session> map=client.get(userid);
		List<Session> channels=new ArrayList<>();
		if(map!=null){
			for(Session value : map.values()){
				channels.add(value);
			}
		}
		return channels;
	}
}

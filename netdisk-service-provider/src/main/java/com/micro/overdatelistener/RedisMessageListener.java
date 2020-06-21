package com.micro.overdatelistener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import com.micro.common.Contanst;

@Component
public class RedisMessageListener implements MessageListener{
	@Autowired
	private RedisService redisService;
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expireKey = new String(message.getBody());
		
		
		if(expireKey.startsWith(Contanst.SHARE)){//分享过期
			try{
				//第一步：调用更新
				String id=expireKey.replace(Contanst.SHARE,"");			
				redisService.overdueShare(id);
				
			}catch(Exception e){
				//第二步：如果失败了，则做备份处理
				
			}
		}else if(expireKey.startsWith(Contanst.RUBBISH)){//回收站过期
			try{
				//第一步：调用更新
				String id=expireKey.replace(Contanst.RUBBISH,"");
				redisService.overdueRubbish(id);
				
			}catch(Exception e){
				//第二步：如果失败了，则做备份处理
			}
		}else if(expireKey.startsWith(Contanst.PREFIX_CHUNK_TEMP)){//切块临时记录过期
			try{
				
				//redisService.deleteTemps(expireKey);
			}catch(Exception e){
				//第二步：如果失败了，则做备份处理				
			}
			
		}
	}

}

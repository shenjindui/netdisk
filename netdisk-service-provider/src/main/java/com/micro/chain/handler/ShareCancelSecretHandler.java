package com.micro.chain.handler;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareCancelRequest;
import com.micro.common.Contanst;
import com.micro.db.dao.DiskShareDao;
import com.micro.model.DiskShare;

@Component
public class ShareCancelSecretHandler extends Handler{
	@Autowired
	private DiskShareDao diskShareDao;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareCancelRequest){
			ShareCancelRequest bean=(ShareCancelRequest) request;
			
			List<String> ids=bean.getSecretIds();
			ids.forEach(id->{		
				DiskShare ds=diskShareDao.findOne(id);
				int type=ds.getType();//0私密链接分享，1好友分享
				int effect=ds.getEffect();//0永久，7表示7天，1表示1天
				if(type==0&&effect!=0){
					stringRedisTemplate.delete(Contanst.SHARE+id);
				}
				
			});
		}else{
			throw new RuntimeException("ShareCancelSecretHandler==参数不对");
		}
	}
}

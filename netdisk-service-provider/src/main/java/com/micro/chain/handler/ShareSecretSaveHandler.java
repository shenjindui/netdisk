package com.micro.chain.handler;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareSecretRequest;
import com.micro.chain.param.ShareSecretResponse;
import com.micro.common.DateUtils;
import com.micro.common.RandomUtils;
import com.micro.db.dao.DiskShareDao;
import com.micro.model.DiskShare;

@Component
public class ShareSecretSaveHandler extends Handler{
	@Autowired
	private DiskShareDao diskShareDao;
	@NacosValue(value="${vueprojecthost}",autoRefreshed=true)
	private String vueprojecthost;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSecretRequest){
			ShareSecretRequest bean=(ShareSecretRequest) request;
			
			String code="";//分享码
			Date endtime=null;//失效时间
			if(bean.getSharetype()==0){//【属于私密分享】0有提取码，1无提取码				
				code=RandomUtils.getRandom(4);
			}
			if(bean.getEffect()!=0){				
				endtime=DateUtils.getSpecialDate(bean.getEffect());
			}
			
			DiskShare share=new DiskShare();
			share.setShareuserid(bean.getUserid());
			share.setShareusername(bean.getUsername());
			share.setSharetime(new Date());
			share.setTitle(bean.getTitle());
			share.setType(0);
			share.setSharetype(bean.getSharetype());
			share.setEffect(bean.getEffect());
			share.setUrl("");
			share.setCode(code);
			share.setEndtime(endtime);
			share.setStatus(0);
			share.setSavecount(0);
			diskShareDao.save(share);
			
			
			String url=vueprojecthost+"/#/sharepwd/"+share.getId().toUpperCase();
			share.setUrl(url);
			diskShareDao.save(share);
			
			//下一个Handler
			bean.setShareid(share.getId());
			this.updateRequest(bean);
			
			ShareSecretResponse res=(ShareSecretResponse) response;
			res.setUrl(url);
			res.setCode(code);
			this.updateResponse(res);
		}else{
			throw new RuntimeException("ShareSecretSaveHandler==参数不对");
		}
	}
}

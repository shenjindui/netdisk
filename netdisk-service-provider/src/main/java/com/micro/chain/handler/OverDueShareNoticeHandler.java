package com.micro.chain.handler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.OverDueShareRequest;
import com.micro.common.DateUtils;
import com.micro.common.NoticeConstant;
import com.micro.db.dao.DiskNoticeDao;
import com.micro.model.DiskNotice;
import com.micro.model.DiskShare;

@Component
public class OverDueShareNoticeHandler extends Handler{
	@Autowired
	private DiskNoticeDao diskNoticeDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof OverDueShareRequest){
			OverDueShareRequest bean=(OverDueShareRequest) request;
			
			DiskShare ds=bean.getDiskShare();
			if(ds!=null){
				if(ds.getStatus()==0){					
					String content="您于"+DateUtils.formatDate(ds.getSharetime(),"yyyy-MM-dd HH:mm:ss")+"分享的文件【"+ds.getTitle()+"】,在"+DateUtils.getCurrentTime()+"已经失效!";
					DiskNotice notice=new DiskNotice();
					notice.setType(NoticeConstant.NOTICE_SHARE_SECRET_OVERDUE);
					notice.setTypename(NoticeConstant.getTypeName(NoticeConstant.NOTICE_SHARE_SECRET_OVERDUE));
					notice.setContent(content);
					notice.setUserid(ds.getShareuserid());
					notice.setUsername(ds.getShareusername());
					notice.setCreatetime(new Date());
					notice.setStatus(0);
					diskNoticeDao.save(notice);
				}
			}
		}else{
			throw new RuntimeException("OverDueShareNoticeHandler==参数不对");
		}
	}
}

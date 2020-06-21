package com.micro.chain.handler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.OverDueRubbishRequest;
import com.micro.common.DateUtils;
import com.micro.common.NoticeConstant;
import com.micro.db.dao.DiskNoticeDao;
import com.micro.model.DiskFileDel;
import com.micro.model.DiskNotice;

@Component
public class OverDueRubbishNoticeHandler extends Handler{
	@Autowired
	private DiskNoticeDao diskNoticeDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof OverDueRubbishRequest){
			OverDueRubbishRequest bean=(OverDueRubbishRequest) request;
			
			DiskFileDel dfd=bean.getDfd();
			if(dfd!=null){
				String filename="";
				if(dfd.getFiletype()==0){
					filename=dfd.getFilename()+"(文件夹)";
				}else{
					filename=dfd.getFilename()+"(文件)";				
				}
				
				String content="您于"+DateUtils.formatDate(dfd.getDeletetime(),"yyyy-MM-dd HH:mm:ss")+"删除的"+filename+",在"+DateUtils.getCurrentTime()+"已到期,系统自动清空回收站!";
				DiskNotice notice=new DiskNotice();
				notice.setType(NoticeConstant.NOTICE_RUBBISH_OVERDUE);
				notice.setTypename(NoticeConstant.getTypeName(NoticeConstant.NOTICE_RUBBISH_OVERDUE));
				notice.setContent(content);
				notice.setUserid(dfd.getCreateuserid());
				notice.setUsername(dfd.getCreateusername());
				notice.setCreatetime(new Date());
				notice.setStatus(0);
				diskNoticeDao.save(notice);
			}
		}else{
			throw new RuntimeException("OverDueRubbishNoticeHandler==参数不对");
		}
	}
}

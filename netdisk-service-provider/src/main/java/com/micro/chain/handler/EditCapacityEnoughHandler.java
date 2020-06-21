package com.micro.chain.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.EditRequest;
import com.micro.chain.param.EditResponse;
import com.micro.db.dao.DiskFileDao;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.disk.service.UserCapacityService;
import com.micro.model.DiskFile;

@Component
public class EditCapacityEnoughHandler extends Handler {
	@Autowired
	private DiskFileDao diskFileDao;
	@Autowired
	private UserCapacityService userCapacityService;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof EditRequest){
			EditRequest bean=(EditRequest) request;
			
			
			DiskFile df=diskFileDao.findOne(bean.getFileid());
			if(df==null){
				throw new RuntimeException("文件ID不存在");
			}
			
			//2.容量
			UserCapacityBean capacity=userCapacityService.findUserCapacity(df.getCreateuserid());
			long size=0l;
			int type=0;
			long oldsize=df.getFilesize();
			long newsize=bean.getBytes().length;
			if(oldsize<newsize){//编辑之后的大小比原来的大
				size=newsize-oldsize;
				if(size>=(capacity.getTotalcapacity()-capacity.getUsedcapacity())){
					throw new RuntimeException("您的存储空间不足,请联系管理员");
				}
				
				type=0;//0（新增已用容量，减少总容量）
			}else{//编辑之后的大小比原来的小
				size=oldsize-newsize;
				type=1;//1（减少已用容量，新增总容量）
			}
			
			//下一个Handler
			bean.setType(type);
			bean.setSize(newsize);
			bean.setFilename(df.getFilename());
			bean.setPrevfilemd5(df.getFilemd5());
			bean.setUserid(df.getCreateuserid());
			bean.setUsername(df.getCreateusername());
			this.updateRequest(bean);
			
			EditResponse res=(EditResponse) response;
			res.setFilename(df.getFilename());
			this.updateResponse(res);
		}else{
			throw new RuntimeException("EditCapacityEnoughHandler==参数不对");
		}
	}
}

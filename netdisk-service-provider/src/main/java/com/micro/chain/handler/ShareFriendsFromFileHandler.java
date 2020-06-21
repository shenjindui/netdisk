package com.micro.chain.handler;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareFriendsRequest;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskShareFileDao;
import com.micro.model.DiskFile;
import com.micro.model.DiskShareFile;

@Component
public class ShareFriendsFromFileHandler extends Handler{
	@Autowired
	private DiskFileDao diskFileDao;
	@Autowired
	private DiskShareFileDao diskShareFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareFriendsRequest){
			ShareFriendsRequest bean=(ShareFriendsRequest) request;
			
			if(bean.getType()==0){
				List<String> ids=bean.getIds();
				
				for(String id:ids){				
					DiskFile file=diskFileDao.findOne(id);
					if(file==null){
						throw new RuntimeException("分享的记录不存在,id="+id);
					}
					DiskShareFile shareFile=new DiskShareFile();
					BeanUtils.copyProperties(file, shareFile);
					shareFile.setId(null);
					shareFile.setShareid(bean.getShareid());
					shareFile.setPid("0");
					diskShareFileDao.save(shareFile);
					dgFile(bean.getUserid(),id,shareFile.getId() ,bean.getShareid());
				}
			}
		}else{
			throw new RuntimeException("ShareFriendsFromFileHandler==参数不对");
		}
	}
	private void dgFile(String userid,String filepid,String sharepid,String shareid){
		List<DiskFile> files=diskFileDao.findListByPid(userid,filepid);
		if(!CollectionUtils.isEmpty(files)){
			for(DiskFile file:files){
				DiskShareFile shareFile=new DiskShareFile();
				BeanUtils.copyProperties(file, shareFile);
				shareFile.setId(null);
				shareFile.setShareid(shareid);
				shareFile.setPid(sharepid);
				diskShareFileDao.save(shareFile);
				dgFile(userid,file.getId(),shareFile.getId(),shareid);
			}
		}
	}
}

package com.micro.chain.handler;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppMergeRequest;
import com.micro.chain.param.AppMergeResponse;
import com.micro.chain.param.CreateResponse;
import com.micro.chain.param.MergeRequest;
import com.micro.db.dao.DiskAppFileDao;
import com.micro.model.DiskAppFile;

@Component
public class AppMergeSaveFileHandler extends Handler{
	@Autowired
	private DiskAppFileDao diskAppFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppMergeRequest){
			AppMergeRequest bean=(AppMergeRequest) request;
			String appid=bean.getAppId();
			String businessid=bean.getBusinessid();
			String businesstype=bean.getBusinesstype();
			String filemd5=bean.getFilemd5();
			String filename=bean.getFilename();
			List<DiskAppFile> files=diskAppFileDao.findLists(appid, businessid, businesstype, filemd5, filename);
			String fileid="";
			if(CollectionUtils.isEmpty(files)){
				//是否允许businessid+businesstype下存在多个文件
				Boolean allowMultiple=bean.getAllowMultiple();
				if(!allowMultiple){
					List<DiskAppFile> lists=diskAppFileDao.findListByBusinessidAndBusinesstype(appid, businessid, businesstype);
					if(!CollectionUtils.isEmpty(lists)){
						if(lists.size()>1){
							throw new RuntimeException("appId="+appid+"->bussinessId="+businessid+"->businessType="+businesstype+"；已经存在多条记录!");
						}else{
							fileid=update(bean, lists.get(0));
						}
					}else{						
						fileid=save(bean);
					}
				}else{			
					fileid=save(bean);
				}
			}else{
				fileid=files.get(0).getId();
			}
			
			if(response instanceof AppMergeResponse){
				AppMergeResponse res=(AppMergeResponse) response;
				res.setFileid(fileid);
				this.updateResponse(res);
			}
		}else{
			throw new RuntimeException("AppMergeSaveFileHandler==参数不对");
		}				
	}
	private String update(AppMergeRequest bean,DiskAppFile file){
		file.setAppid(bean.getAppId());
		file.setBusinessid(bean.getBusinessid());
		file.setBusinesstype(bean.getBusinesstype());
		file.setFilename(bean.getFilename());
		file.setFilesize(bean.getFilesize());
		file.setFilesuffix(bean.getFilesuffix());
		file.setTypecode(bean.getTypecode());
		file.setFilemd5(bean.getFilemd5());
		file.setDelstatus(0);
		file.setCreateuserid(bean.getUserid());
		file.setCreateusername(bean.getUsername());
		file.setCreatetime(new Date());
		diskAppFileDao.save(file);
		return file.getId();
	}
	private String save(AppMergeRequest bean){
		DiskAppFile file=new DiskAppFile();
		file.setAppid(bean.getAppId());
		file.setBusinessid(bean.getBusinessid());
		file.setBusinesstype(bean.getBusinesstype());
		file.setFilename(bean.getFilename());
		file.setFilesize(bean.getFilesize());
		file.setFilesuffix(bean.getFilesuffix());
		file.setTypecode(bean.getTypecode());
		file.setFilemd5(bean.getFilemd5());
		file.setDelstatus(0);
		file.setCreateuserid(bean.getUserid());
		file.setCreateusername(bean.getUsername());
		file.setCreatetime(new Date());
		file.setIsbreak(0);
		diskAppFileDao.save(file);
		return file.getId();
	}
}

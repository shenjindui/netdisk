package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareSaveRequest;
import com.micro.common.DateUtils;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.modeltree.DiskShareFileTree;

@Component
public class ShareSaveInsertHandler extends Handler{
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSaveRequest){
			ShareSaveRequest bean=(ShareSaveRequest) request;
			
			List<String> solrids=new ArrayList<>();
			dgSaveFromShare(bean.getFolderid(), bean.getUserid(), bean.getUsername(), bean.getFiles(),solrids);
			
			//下一个Handler
			bean.setSolrids(solrids);
			this.updateRequest(bean);
			
		}else{
			throw new RuntimeException("ShareSaveInsertHandler==参数不对");
		}
	}
	
	public void dgSaveFromShare(String pid,String userid,String username,List<DiskShareFileTree> files,List<String> solrids){
		try{
			if(!CollectionUtils.isEmpty(files)){
				for(DiskShareFileTree tree:files){			
					DiskFile diskFile=null;
					if(tree.getFiletype()==0){
						diskFile=diskFileDao.findFolderNameIsExist(userid,pid, tree.getFilename());
					}else if(tree.getFiletype()==1){
						diskFile=diskFileDao.findFileNameIsExist(userid,pid, tree.getFilemd5(), tree.getFilename());
					}
					String fileid="";
					if(diskFile==null){
						DiskFile df=new DiskFile();
						BeanUtils.copyProperties(tree, df);
						df.setId(null);
						df.setPid(pid);
						df.setCreateuserid(userid);
						df.setCreateusername(username);
						df.setCreatetime(new Date());	
						diskFileDao.save(df);
						
						fileid=df.getId();
					}else{
						if(tree.getFiletype()==0){
							DiskFile df=new DiskFile();
							BeanUtils.copyProperties(tree, df);
							df.setId(null);
							df.setPid(pid);
							df.setFilename(tree.getFilename()+"("+DateUtils.formatDate(new Date(),"来源分享,yyyy-MM-dd HH:mm:ss.S")+")");
							df.setCreateuserid(userid);
							df.setCreateusername(username);
							df.setCreatetime(new Date());
							diskFileDao.save(df);
							
							fileid=df.getId();
						}else{						
							fileid=diskFile.getId();
						}
					}
					
					solrids.add(fileid);
					dgSaveFromShare(fileid, userid, username, tree.getChilren(),solrids);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}

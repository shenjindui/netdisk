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
import com.micro.chain.param.CopyRequest;
import com.micro.common.DateUtils;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.modeltree.DiskFileTree;

@Component
public class CopySaveHandler extends Handler {
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof CopyRequest){
			CopyRequest bean=(CopyRequest) request;
			List<String> newFileIds=new ArrayList<>();
			dgCopy(bean.getUserid(), bean.getFiles(), bean.getFolderid(),newFileIds);
			
			//写到下个Handler
			bean.setNewFileIds(newFileIds);
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("CopySaveHandler==参数不对");
		}
	}
	
	public void dgCopy(String userid,List<DiskFileTree> files,String folderid,List<String> newFileIds){
		if(!CollectionUtils.isEmpty(files)){			
			for(DiskFileTree tree:files){
				DiskFile diskFile=null;
				if(tree.getFiletype()==0){
					diskFile=diskFileDao.findFolderNameIsExist(userid,folderid, tree.getFilename());
				}else if(tree.getFiletype()==1){
					diskFile=diskFileDao.findFileNameIsExist(userid,folderid, tree.getFilemd5(), tree.getFilename());
				}
				
				String fileid="";
				if(diskFile==null){
					DiskFile dfNew=new DiskFile();
					BeanUtils.copyProperties(tree, dfNew);
					dfNew.setId(null);
					dfNew.setPid(folderid);
					dfNew.setCreatetime(new Date());
					diskFileDao.save(dfNew);
					fileid=dfNew.getId();
				}else{
					if(tree.getFiletype()==0){//如果存在相同的文件夹，则加时间标识来区分
						DiskFile dfNew=new DiskFile();
						BeanUtils.copyProperties(tree, dfNew);
						dfNew.setId(null);
						dfNew.setPid(folderid);
						dfNew.setFilename(tree.getFilename()+"("+DateUtils.formatDate(new Date(),"复制于yyyy-MM-dd HH:mm:ss.S")+")");
						dfNew.setCreatetime(new Date());
						diskFileDao.save(dfNew);
						fileid=dfNew.getId();
					}else{//如果存在相同的文件，则跳过
						fileid=diskFile.getId();
					}
				}
				
				newFileIds.add(fileid);
				
				dgCopy(userid,tree.getChildren(),fileid,newFileIds);
			}
		}
	}
}

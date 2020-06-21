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
import com.micro.chain.param.RubbishRecoverRequest;
import com.micro.common.DateUtils;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.model.DiskFile;
import com.micro.modeltree.DiskFileDelTree;

@Component
public class RubbishReoverInsertHandler extends Handler{
	@Autowired
	private DiskFileDao diskFileDao;
	@Autowired
	private DiskFileDelDao diskFileDelDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof RubbishRecoverRequest){
			RubbishRecoverRequest bean=(RubbishRecoverRequest) request;
			
			List<DiskFileDelTree> files=bean.getTrees();
			List<String> rediskeys=new ArrayList<>();
			List<String> solrids=new ArrayList<>();
			String pid=bean.getFolderid();
			String userid=bean.getUserid();
			dgRecover(files, solrids,rediskeys, pid, userid);
			
			//下一个Handler
			bean.setSolrids(solrids);
			bean.setRediskeys(rediskeys);
			this.updateRequest(request);
		}else{
			throw new RuntimeException("RubbishReoverInsertHandler==参数不对");
		}
	}
	
	public void dgRecover(List<DiskFileDelTree> files,List<String> solrids,List<String> rediskeys,String pid,String userid){
		if(!CollectionUtils.isEmpty(files)){			
			for(DiskFileDelTree tree:files){
				DiskFile diskFile=null;
				if(tree.getFiletype()==0){
					diskFile=diskFileDao.findFolderNameIsExist(userid,pid, tree.getFilename());
				}else if(tree.getFiletype()==1){
					diskFile=diskFileDao.findFileNameIsExist(userid,pid, tree.getFilemd5(), tree.getFilename());
				}
				
				//添加disk_file
				String fileid="";
				if(diskFile==null){					
					DiskFile df=new DiskFile();
					BeanUtils.copyProperties(tree, df);
					df.setId(null);
					df.setCreatetime(new Date());
					df.setPid(pid);
					diskFileDao.save(df);
					fileid=df.getId();
				}else{
					if(tree.getFiletype()==0){
						DiskFile df=new DiskFile();
						BeanUtils.copyProperties(tree, df);
						df.setId(null);
						df.setFilename(tree.getFilename()+"("+DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss.S")+")");
						df.setCreatetime(new Date());
						df.setPid(pid);
						diskFileDao.save(df);
						fileid=df.getId();
					}else{						
						fileid=diskFile.getId();
					}
				}
				
				//收集数据
				solrids.add(fileid);
				rediskeys.add(tree.getId());
				
				//删除disk_file_del
				diskFileDelDao.deleteById(tree.getId());
				
				//递归
				dgRecover(tree.getChildren(),solrids,rediskeys, fileid,userid);
			}
		}
	}
}

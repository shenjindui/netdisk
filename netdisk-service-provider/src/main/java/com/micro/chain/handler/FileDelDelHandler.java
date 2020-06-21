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
import com.micro.chain.param.FileDelRequest;
import com.micro.db.dao.DiskAlbumFileDao;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.model.DiskFileDel;
import com.micro.modeltree.DiskFileTree;

@Component
public class FileDelDelHandler extends Handler {
	@Autowired
	private DiskFileDao diskFileDao;
	@Autowired
	private DiskFileDelDao diskFileDelDao;
	@Autowired
	private DiskAlbumFileDao diskAlbumFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof FileDelRequest){
			FileDelRequest bean=(FileDelRequest) request;
			
			List<String> rubbishs=new ArrayList<>();
			List<DiskFileTree> files=bean.getFiles();
			dgDelete(files,"0",rubbishs);
			
			//写到下一个Handler
			bean.setRubbishs(rubbishs);
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("FileDelDelHandler==参数不对");
		}
	}
	
	private void dgDelete(List<DiskFileTree> files,String pid,List<String> rubbishs){
		try{
			
			if(!CollectionUtils.isEmpty(files)){
				for(DiskFileTree tree:files){
					//保存disk_file_del
					DiskFileDel del=new DiskFileDel();
					BeanUtils.copyProperties(tree, del);
					del.setId(null);
					del.setPid(pid);
					del.setDeletetime(new Date());
					diskFileDelDao.save(del);
					
					//收集ID
					rubbishs.add(del.getId());
					
					//删除相册关联
					if("picture".equals(tree.getTypecode())){
						diskAlbumFileDao.deleteByFileid(tree.getId());
					}
					
					//删除disk_file
					diskFileDao.delete(tree.getId());
					
					//递归
					dgDelete(tree.getChildren(), del.getId(),rubbishs);
				}
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}

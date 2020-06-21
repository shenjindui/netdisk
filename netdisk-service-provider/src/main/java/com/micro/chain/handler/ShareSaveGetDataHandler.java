package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareSaveRequest;
import com.micro.db.dao.DiskShareFileDao;
import com.micro.model.DiskShareFile;
import com.micro.modeltree.DiskShareFileTree;
import com.micro.utils.CapacityBean;

@Component
public class ShareSaveGetDataHandler extends Handler{
	@Autowired
	private DiskShareFileDao diskShareFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSaveRequest){
			ShareSaveRequest bean=(ShareSaveRequest) request;
	
			List<String> ids=bean.getIds();
			CapacityBean capacityBean=new CapacityBean(0l);
			List<DiskShareFileTree> files=new ArrayList<>();
			
			for(String id:ids){			
				//查询
				DiskShareFile shareFile=diskShareFileDao.findOne(id);
				
				//累加容量
				capacityBean.setTotalsize(capacityBean.getTotalsize()+shareFile.getFilesize());
				
				
				DiskShareFileTree tree=new DiskShareFileTree();
				BeanUtils.copyProperties(shareFile, tree);
				List<DiskShareFileTree> chilren=new ArrayList<DiskShareFileTree>();
				
				dgSaveFromShareSearch(bean.getShareid(), shareFile.getId(), chilren, capacityBean);

				tree.setChilren(chilren);
				files.add(tree);
			}
			
			//写到下一个Handler
			bean.setCapacity(capacityBean.getTotalsize());
			bean.setFiles(files);
			this.updateRequest(bean);
			
		}else{
			throw new RuntimeException("ShareSaveGetDataHandler==参数不对");
		}
	}
	public void dgSaveFromShareSearch(String shareid,String pid,List<DiskShareFileTree> files,CapacityBean capacityBean){
		List<DiskShareFile> sharefilefiles=diskShareFileDao.findListChild(shareid, pid);
		if(!CollectionUtils.isEmpty(sharefilefiles)){
			for(DiskShareFile file:sharefilefiles){				
				capacityBean.setTotalsize(capacityBean.getTotalsize()+file.getFilesize());
				
				DiskShareFileTree tree=new DiskShareFileTree();
				BeanUtils.copyProperties(file, tree);
				List<DiskShareFileTree> chilren=new ArrayList<DiskShareFileTree>();
				dgSaveFromShareSearch(shareid, file.getId(), chilren, capacityBean);
				tree.setChilren(chilren);
				files.add(tree);
			}
		}
		
	}
}

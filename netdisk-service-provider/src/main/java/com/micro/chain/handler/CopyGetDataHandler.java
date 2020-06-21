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
import com.micro.chain.param.CopyRequest;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.modeltree.DiskFileTree;
import com.micro.utils.CapacityBean;

@Component
public class CopyGetDataHandler extends Handler {
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof CopyRequest){
			CopyRequest bean=(CopyRequest) request;
			
			CapacityBean capacityBean=new CapacityBean(0l);
			List<String> ids=bean.getIds();
			List<DiskFileTree> files=new ArrayList<>();
			
			for(String id:ids){
				DiskFile df=diskFileDao.findOne(id);
				capacityBean.setTotalsize(capacityBean.getTotalsize()+df.getFilesize());
				
				DiskFileTree tree=new DiskFileTree();				
				BeanUtils.copyProperties(df, tree);
				List<DiskFileTree> children=new ArrayList<DiskFileTree>();
				
				dgCopySearch(bean.getUserid(),df.getId(), children, capacityBean);
				
				tree.setChildren(children);
				files.add(tree);
			}
			
			//下一个Handler
			bean.setFiles(files);
			bean.setCapacity(capacityBean.getTotalsize());
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("CopyGetDataHandler==参数不对");
		}
	}
	public void dgCopySearch(String userid,String pid,List<DiskFileTree> files,CapacityBean capacityBean){
		List<DiskFile> dfs=diskFileDao.findListByPid(userid,pid);
		if(!CollectionUtils.isEmpty(dfs)){
			for(DiskFile df:dfs){
				//容量累加
				capacityBean.setTotalsize(capacityBean.getTotalsize()+df.getFilesize());
				
				//获取数据
				DiskFileTree tree=new DiskFileTree();				
				BeanUtils.copyProperties(df, tree);
				List<DiskFileTree> children=new ArrayList<DiskFileTree>();
				dgCopySearch(userid,df.getId(), children, capacityBean);
				tree.setChildren(children);
				files.add(tree);
			}
		}
	}
}

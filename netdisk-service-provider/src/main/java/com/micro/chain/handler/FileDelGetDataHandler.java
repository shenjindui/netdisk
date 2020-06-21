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
import com.micro.chain.param.FileDelRequest;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.modeltree.DiskFileTree;
import com.micro.utils.CapacityBean;

@Component
public class FileDelGetDataHandler extends Handler{
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof FileDelRequest){
			FileDelRequest bean=(FileDelRequest) request;
			
			List<String> ids=bean.getIds();
			List<DiskFileTree> files=new ArrayList<>();
			CapacityBean capacityBean=new CapacityBean(0l);//计算总大小
			
			for(int i=0;i<ids.size();i++){
				String id=ids.get(i);
				int index=i+1;
				DiskFile df=diskFileDao.findOne(id);
				if(df==null){
					throw new RuntimeException("选中的第"+index+"个文件(ID="+id+")不存在");
				}
				
				//获取数据
				DiskFileTree tree=new DiskFileTree();				
				BeanUtils.copyProperties(df, tree);
				List<DiskFileTree> children=new ArrayList<DiskFileTree>();
				
				dgDeleteSearch(df.getCreateuserid(),df.getId(), children, capacityBean);
				
				tree.setChildren(children);
				files.add(tree);
			}
			
			//写入下一个Handler
			bean.setFiles(files);
			bean.setCapacity(capacityBean.getTotalsize());
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("FileDelGetDataHandler==参数不对");
		}
	}
	private void dgDeleteSearch(String userid,String pid,List<DiskFileTree> files,CapacityBean capacityBean){
		List<DiskFile> dfs=diskFileDao.findListByPid(userid,pid);
		if(!CollectionUtils.isEmpty(dfs)){
			for(DiskFile df:dfs){
				//计算总大小
				capacityBean.setTotalsize(capacityBean.getTotalsize()+df.getFilesize());
				
				//获取数据
				DiskFileTree tree=new DiskFileTree();				
				BeanUtils.copyProperties(df, tree);
				List<DiskFileTree> children=new ArrayList<DiskFileTree>();
				dgDeleteSearch(userid,df.getId(), children, capacityBean);
				tree.setChildren(children);
				files.add(tree);
			}
		}
	}
}

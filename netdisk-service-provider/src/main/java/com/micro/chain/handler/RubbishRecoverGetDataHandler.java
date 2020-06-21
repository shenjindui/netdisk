package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.RubbishRecoverRequest;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.model.DiskFileDel;
import com.micro.modeltree.DiskFileDelTree;
import com.micro.utils.CapacityBean;

@Component
public class RubbishRecoverGetDataHandler extends Handler{
	@Autowired
	private DiskFileDelDao diskFileDelDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof RubbishRecoverRequest){
			RubbishRecoverRequest bean=(RubbishRecoverRequest) request;
			
			List<String> ids=bean.getIds();
			List<DiskFileDelTree> trees=new ArrayList<>();
			CapacityBean capacityBean=new CapacityBean(0l);
			
			for(int i=0;i<ids.size();i++){
				String id=ids.get(i);
				int index=i+1;
				DiskFileDel del=diskFileDelDao.findOne(id);
				if(del==null){
					throw new RuntimeException("选中的第"+index+"条记录已经被删除,请刷新列表!");
				}
				capacityBean.setTotalsize(capacityBean.getTotalsize()+del.getFilesize());
				
				DiskFileDelTree tree=converBean(del);
				List<DiskFileDelTree> children=new ArrayList<DiskFileDelTree>();
				
				dgRecoverSearch(del.getCreateuserid(),del.getId(), capacityBean, children);
				
				tree.setChildren(children);
				trees.add(tree);
			}
			
			//下一个Handler
			bean.setTrees(trees);
			bean.setCapacity(capacityBean.getTotalsize());
			this.updateRequest(bean);
		}else{
			throw new RuntimeException("RubbishRecoverGetDataHandler==参数不对");
		}
	}
	
	public void dgRecoverSearch(String userid,String pid,CapacityBean capacityBean,List<DiskFileDelTree> trees){
		List<DiskFileDel> lists=diskFileDelDao.findChildList(userid,pid);
		if(!CollectionUtils.isEmpty(lists)){
			for(DiskFileDel bean:lists){
				capacityBean.setTotalsize(capacityBean.getTotalsize()+bean.getFilesize());
				
				DiskFileDelTree tree=converBean(bean);
				List<DiskFileDelTree> children=new ArrayList<DiskFileDelTree>();
				dgRecoverSearch(userid,bean.getId(),capacityBean, children);
				tree.setChildren(children);
				trees.add(tree);
			}
		}
	}
	
	public DiskFileDelTree converBean(DiskFileDel del){
		DiskFileDelTree tree=new DiskFileDelTree();
		tree.setId(del.getId());
		tree.setPid(del.getPid());
		tree.setFilename(del.getFilename());
		tree.setFilesize(del.getFilesize());
		tree.setFilesuffix(del.getFilesuffix());
		tree.setTypecode(del.getTypecode());
		tree.setFilemd5(del.getFilemd5());
		tree.setFiletype(del.getFiletype());
		tree.setCreateuserid(del.getCreateuserid());
		tree.setCreateusername(del.getCreateusername());
		tree.setCreatetime(del.getCreatetime());
		tree.setDeletetime(del.getDeletetime());
		tree.setThumbnailurl(del.getThumbnailurl());
		tree.setImgsize(del.getImgsize());
		return tree;
	}
}

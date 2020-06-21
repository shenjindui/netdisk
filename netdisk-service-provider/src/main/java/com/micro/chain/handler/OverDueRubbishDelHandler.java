package com.micro.chain.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.OverDueRubbishRequest;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.model.DiskFileDel;

@Component
public class OverDueRubbishDelHandler extends Handler{
	@Autowired
	private DiskFileDelDao diskFileDelDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof OverDueRubbishRequest){
			OverDueRubbishRequest bean=(OverDueRubbishRequest) request;
			
			if(bean.getDfd()!=null){
				//删除本节点
				diskFileDelDao.delete(bean.getId());

				//递归删除
				dgDel(bean.getDfd().getCreateuserid(),bean.getId());
			}
		}else{
			throw new RuntimeException("OverDueRubbishDelHandler==参数不对");
		}
	}
	public void dgDel(String userid,String pid){
		//查询子节点
		List<DiskFileDel> lists=diskFileDelDao.findChildList(userid,pid);
		if(!CollectionUtils.isEmpty(lists)){
			for(DiskFileDel map:lists){
				String id=map.getId();
				//删除本节点
				diskFileDelDao.delete(id);
				
				//递归删除
				dgDel(userid,id);
			}
		}
	}
}

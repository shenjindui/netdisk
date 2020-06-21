package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.FolderRequest;
import com.micro.common.DateUtils;
import com.micro.common.IconConstant;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.search.bean.FileSearchBean;
import com.micro.search.context.SearchContext;
import com.micro.search.service.FileSearchService;

@Component
public class FolerSolrHandler extends Handler{
	@Autowired
	private SearchContext searchContext;
	@Autowired
	private DiskFileDao diskFileDao;
	private JsonUtils jsonUtils=new JsonJackUtils();
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof FolderRequest){
			FolderRequest bean=(FolderRequest) request;
			
			//获取pname
			List<Map<String,Object>> lists=new ArrayList<>();
			getPname(lists, bean.getPid());
			String pname=jsonUtils.objectToJson(lists);
			
			FileSearchBean fsb=new FileSearchBean();
			fsb.setId(bean.getDiskfileid());
			fsb.setFilename(bean.getFilename());
			fsb.setPid(bean.getPid());
			fsb.setPname(pname);//
			fsb.setFilemd5("");
			fsb.setFileicon(IconConstant.icon);//图标
			fsb.setTypecode("");
			fsb.setFilesuffix("");
			fsb.setFilesize("");
			fsb.setFiletype("0");
			fsb.setCreateuserid(bean.getUserid());
			fsb.setCreateusername(bean.getUsername());
			fsb.setCreatetime(DateUtils.getCurrentTime());
			searchContext.add(fsb);
		}else{
			throw new RuntimeException("FolerSolrHandler==参数不对");
		}		
	}
	//递归获取pname
	private void getPname(List<Map<String,Object>> lists,String pid){
		if(!"0".equals(pid)){			
			DiskFile df=diskFileDao.findOne(pid);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", df.getId());
			map.put("name", df.getFilename());
			lists.add(map);
			getPname(lists, df.getPid());
		}
	}
}

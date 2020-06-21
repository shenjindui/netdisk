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
import com.micro.chain.param.RubbishRecoverRequest;
import com.micro.common.CapacityUtils;
import com.micro.common.DateUtils;
import com.micro.common.IconConstant;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskTypeSuffixDao;
import com.micro.model.DiskFile;
import com.micro.model.DiskTypeSuffix;
import com.micro.search.bean.FileSearchBean;
import com.micro.search.context.SearchContext;
import com.micro.search.service.FileSearchService;

@Component
public class RubbishRecoverSolrHandler extends Handler{
	@Autowired
	private SearchContext searchContext;
	@Autowired
	private DiskTypeSuffixDao diskTypeSuffixDao;
	@Autowired
	private DiskFileDao diskFileDao;
	private JsonUtils jsonUtils=new JsonJackUtils();
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof RubbishRecoverRequest){
			RubbishRecoverRequest bean=(RubbishRecoverRequest) request;
			
			for(String fileid:bean.getSolrids()){
				DiskFile df=diskFileDao.findOne(fileid);
				
				//获取图标
				String icon="";
				if(df.getFiletype()==0){
					icon=IconConstant.icon;
				}else{
					DiskTypeSuffix dts=diskTypeSuffixDao.findBySuffix(df.getFilesuffix());
					icon=dts.getIcon();
				}
				
				//获取pname
				List<Map<String,Object>> lists=new ArrayList<>();
				getPname(lists, df.getPid());
				String pname=jsonUtils.objectToJson(lists);
				
				FileSearchBean fsb=new FileSearchBean();
				fsb.setId(df.getId());
				fsb.setFilename(df.getFilename());
				fsb.setPid(df.getPid());
				fsb.setPname(pname);//
				fsb.setFilemd5(df.getFilemd5());
				
				fsb.setFileicon(icon);//图标
				
				fsb.setTypecode(df.getTypecode());
				fsb.setFilesuffix(df.getFilesuffix());
				fsb.setFilesize(CapacityUtils.convert(df.getFilesize()));
				fsb.setFiletype(df.getFiletype()+"");
				fsb.setCreateuserid(df.getCreateuserid());
				fsb.setCreateusername(df.getCreateusername());
				fsb.setCreatetime(DateUtils.formatDate(df.getCreatetime(),"yyyy-MM-dd HH:mm:ss"));
				searchContext.add(fsb);
			}
		}else{
			throw new RuntimeException("RubbishRecoverSolrHandler==参数不对");
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

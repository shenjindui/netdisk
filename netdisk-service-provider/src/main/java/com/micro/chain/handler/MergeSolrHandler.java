package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MergeRequest;
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
public class MergeSolrHandler extends Handler{
	@Autowired
	private SearchContext searchContext;
	@Autowired
	private DiskTypeSuffixDao diskTypeSuffixDao;
	@Autowired
	private DiskFileDao diskFileDao;
	private JsonUtils jsonUtils=new JsonJackUtils();
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MergeRequest){
			MergeRequest bean=(MergeRequest) request;
			
			//文件夹
			if(!CollectionUtils.isEmpty(bean.getFolders())){
				//目录结构如下：['aa','bb','cc']
				
				for(int i=0;i<bean.getFolders().size();i++){
					DiskFile df=bean.getFolders().get(i);
					List<Map<String,Object>> lists=new ArrayList<>();
					if(i>0){						
						for(int y=0;y<i;y++){
							Map<String,Object> map=new HashMap<>();
							map.put("id", bean.getFolders().get(y).getId());
							map.put("name", bean.getFolders().get(y).getFilename());
							lists.add(map);
						}
					}
					String pname=jsonUtils.objectToJson(lists);
					
					FileSearchBean fsb=new FileSearchBean();
					fsb.setId(df.getId());
					fsb.setFilename(df.getFilename());
					fsb.setPid(df.getPid());
					fsb.setPname(pname);//
					fsb.setFilemd5("");
					fsb.setFileicon(IconConstant.icon);//图标
					fsb.setTypecode("");
					fsb.setFilesuffix("");
					fsb.setFilesize("");
					fsb.setFiletype("0");
					fsb.setCreateuserid(df.getCreateuserid());
					fsb.setCreateusername(df.getCreateusername());
					fsb.setCreatetime(DateUtils.formatDate(df.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
					searchContext.add(fsb);
				}
			}
			
			//文件
			if(!bean.isExistindiskfile()){
				//根据后缀获取图标
				DiskTypeSuffix dts=diskTypeSuffixDao.findBySuffix(bean.getFilesuffix());
				//获取pname
				List<Map<String,Object>> lists=new ArrayList<>();
				getPname(lists, bean.getPid());
				String pname=jsonUtils.objectToJson(lists);
				
				FileSearchBean fsb=new FileSearchBean();
				fsb.setId(bean.getDiskfileid());
				fsb.setFilename(bean.getFilename());
				fsb.setPid(bean.getPid());
				fsb.setPname(pname);//
				fsb.setFilemd5(bean.getFilemd5());
				fsb.setFileicon(dts.getIcon());//图标
				fsb.setTypecode(bean.getTypecode());
				fsb.setFilesuffix(bean.getFilesuffix());
				fsb.setFilesize(CapacityUtils.convert(bean.getTotalSize()));
				fsb.setFiletype("1");
				fsb.setCreateuserid(bean.getUserid());
				fsb.setCreateusername(bean.getUsername());
				fsb.setCreatetime(DateUtils.getCurrentTime());
				searchContext.add(fsb);
			}
			
		}else{
			throw new RuntimeException("MergeCapacityUpdateHandler==参数不对");
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

package com.micro.search.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.search.bean.FileSearchBean;
import com.micro.search.bean.Page;
import com.micro.search.service.FileSearchService;
import com.micro.search.utils.SpringUtils;

@Component
public class SearchContext {
	@Autowired
	private SpringUtils springUtils;
	
	@NacosValue(value="${searchtype}",autoRefreshed=true)
    private String searchtype;
	
	public Page<FileSearchBean> search(String filename,String userid,Integer page,Integer limit){
		Object obj=springUtils.getBean(searchtype);
		if(obj!=null){
            FileSearchService fileSearchService=(FileSearchService)obj;
        	return fileSearchService.search(filename, userid, page, limit);
        }else{
        	throw new RuntimeException("找不到标识searchtype=="+searchtype);
        }
	}
	public void add(FileSearchBean bean){
		Object obj=springUtils.getBean(searchtype);
		if(obj!=null){
            FileSearchService fileSearchService=(FileSearchService)obj;
        	fileSearchService.add(bean);
        }else{
        	throw new RuntimeException("找不到标识searchtype=="+searchtype);
        }
	}
	public void delete(String id){
		Object obj=springUtils.getBean(searchtype);
		if(obj!=null){
            FileSearchService fileSearchService=(FileSearchService)obj;
        	fileSearchService.delete(id);
        }else{
        	throw new RuntimeException("找不到标识searchtype=="+searchtype);
        }
	}
	public void deleteAll(){
		Object obj=springUtils.getBean(searchtype);
		if(obj!=null){
            FileSearchService fileSearchService=(FileSearchService)obj;
        	fileSearchService.deleteAll();
        }else{
        	throw new RuntimeException("找不到标识searchtype=="+searchtype);
        }
	}
	public Long findCount(){
		Object obj=springUtils.getBean(searchtype);
		if(obj!=null){
            FileSearchService fileSearchService=(FileSearchService)obj;
        	return fileSearchService.findCount();
        }else{
        	throw new RuntimeException("找不到标识searchtype=="+searchtype);
        }
	}
}

package com.micro.chain.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.FileDelRequest;
import com.micro.modeltree.DiskFileTree;
import com.micro.search.context.SearchContext;
import com.micro.search.service.FileSearchService;

@Component
public class FileDelSolrHandler extends Handler {
	@Autowired
	private SearchContext searchContext;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof FileDelRequest){
			FileDelRequest bean=(FileDelRequest) request;
			
			List<DiskFileTree> files=bean.getFiles();
			dgDelete(files);
		}else{
			throw new RuntimeException("FileDelSolrHandler==参数不对");
		}
	}
	private void dgDelete(List<DiskFileTree> files){
		if(!CollectionUtils.isEmpty(files)){
			for(DiskFileTree file:files){
				//删除
				searchContext.delete(file.getId());
				//递归
				dgDelete(file.getChildren());
			}
		}
	}
}

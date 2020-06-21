package com.micro.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.micro.common.CapacityUtils;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.SearchBean;
import com.micro.disk.service.SearchService;
import com.micro.search.bean.FileSearchBean;
import com.micro.search.bean.Page;
import com.micro.search.context.SearchContext;
import com.micro.search.service.FileSearchService;

@Service(interfaceClass=SearchService.class)
@Component
public class SearchServiceImpl implements SearchService{
	@Autowired
	private SearchContext searchContext;

	@Override
	public PageInfo<SearchBean> search(String filename,String userid,Integer page,Integer limit) {
		Page<FileSearchBean> pi=searchContext.search(filename, userid, page, limit);
		PageInfo<SearchBean> pageInfo=new PageInfo<>();
		List<SearchBean> rows=new ArrayList<>();
		if(!CollectionUtils.isEmpty(pi.getRows())){
			for(FileSearchBean bean:pi.getRows()){
				SearchBean row=new SearchBean();
				BeanUtils.copyProperties(bean, row);
				//row.setFilesize(CapacityUtils.convert(row.getFilesize()));
				rows.add(row);
			}
		}
		
		pageInfo.setCode(pi.getCode());
		pageInfo.setMsg(pi.getMsg());
		pageInfo.setPage(pi.getPage());
		pageInfo.setLimit(pi.getLimit());
		pageInfo.setRows(rows);
		pageInfo.setTotalElements(pi.getTotalElements());
		pageInfo.setTotalPage(pi.getTotalPage());
		return pageInfo;
	}

}

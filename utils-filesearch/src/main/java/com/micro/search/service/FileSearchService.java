package com.micro.search.service;

import com.micro.search.bean.FileSearchBean;
import com.micro.search.bean.Page;

public interface FileSearchService {
	public Page<FileSearchBean> search(String filename,String userid,Integer page,Integer limit);
	public void add(FileSearchBean bean);
	public void delete(String id);
	public void deleteAll();
	public Long findCount();
}

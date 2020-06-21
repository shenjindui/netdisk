package com.micro.disk.service;

import com.micro.disk.bean.FileEditHistoryBean;
import com.micro.disk.bean.PageInfo;
public interface FileEditHistoryService {

	public PageInfo<FileEditHistoryBean> findList(Integer page,Integer limit,String fileid);
}

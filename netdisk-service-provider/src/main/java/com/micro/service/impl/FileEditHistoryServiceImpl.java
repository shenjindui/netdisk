package com.micro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Service;
import com.micro.db.jdbc.DiskFileEditJdbc;
import com.micro.disk.bean.FileEditHistoryBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.FileEditHistoryService;

@Service(interfaceClass=FileEditHistoryService.class)
@Component
@Transactional
public class FileEditHistoryServiceImpl implements FileEditHistoryService{
	@Autowired
	private DiskFileEditJdbc diskFileEditJdbc;
	
	@Override
	public PageInfo<FileEditHistoryBean> findList(Integer page,Integer limit,String fileid) {
		return diskFileEditJdbc.findList(page, limit, fileid);
	}

}

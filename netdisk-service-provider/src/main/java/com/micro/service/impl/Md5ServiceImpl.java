package com.micro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Service;
import com.micro.db.jdbc.DiskMd5Jdbc;
import com.micro.disk.bean.Md5Bean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.Md5Service;

@Service(interfaceClass=Md5Service.class)
@Component
@Transactional
public class Md5ServiceImpl implements Md5Service{
	@Autowired
	private DiskMd5Jdbc diskMd5Jdbc;
	
	@Override
	public PageInfo<Md5Bean> findList(Integer page, Integer limit, String filename,String md5) {
		return diskMd5Jdbc.findList(page, limit, filename,md5);
	}

}

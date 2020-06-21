package com.micro.disk.service;

import com.micro.disk.bean.Md5Bean;
import com.micro.disk.bean.PageInfo;

public interface Md5Service {
	public PageInfo<Md5Bean> findList(Integer page,Integer limit,String filename,String md5);
}

package com.micro.disk.service;

import java.util.List;

import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.RubbishBean;

public interface RubbishService {
	public PageInfo<RubbishBean> findPageList(Integer page,Integer limit,String userid);
	public void delete(List<String> ids,String userid);
	public void recover(String folderid,List<String> ids,String userid,String username);
}

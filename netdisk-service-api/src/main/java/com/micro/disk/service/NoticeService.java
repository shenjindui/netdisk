package com.micro.disk.service;

import java.util.List;

import com.micro.disk.bean.NoticeBean;
import com.micro.disk.bean.PageInfo;

public interface NoticeService {

	public PageInfo<NoticeBean> findList(Integer page,Integer limit,String userid);
	public void updateReadStatus(String userid);
	public void delete(String userid);
	
	public List<NoticeBean> findNotices(String userid);
	public int findNoticesCount(String userid);
}

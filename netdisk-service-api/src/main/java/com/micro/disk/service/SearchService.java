package com.micro.disk.service;

import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.SearchBean;

public interface SearchService {
	/**
	 * 个人用户查询自己数据的
	 * @param filename
	 * @param userid
	 * @param page
	 * @param limit
	 * @return
	 */
	public PageInfo<SearchBean> search(String filename,String userid,Integer page,Integer limit);
}

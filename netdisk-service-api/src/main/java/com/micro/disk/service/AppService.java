package com.micro.disk.service;

import com.micro.disk.bean.AppBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.UserBean;

public interface AppService {
	public void save(AppBean bean);
	public void update(AppBean bean);
	public void delete(String id);
	public AppBean findOne(String id);
	/**
	 * 应用注册分页类别
	 * @param page
	 * @param limit
	 * @param name
	 * @return
	 */
	public PageInfo<AppBean> findPageList(Integer page,Integer limit,String name);
	/**
	 * 查看每个应用下的用户
	 * @param page
	 * @param limit
	 * @param appid
	 * @param username
	 * @return
	 */
	public PageInfo<UserBean> findUserListByAppid(Integer page,Integer limit,String appid,String username);
	/**
	 * 判断APPID是否存在
	 * @param appid
	 * @return
	 */
	public boolean checkAppID(String appid);
}

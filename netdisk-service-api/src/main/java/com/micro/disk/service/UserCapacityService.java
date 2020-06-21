package com.micro.disk.service;

import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.disk.bean.UserCapacityHistoryBean;

public interface UserCapacityService {
	/**
	 * 获取用户的容量
	 * @param userid
	 * @return
	 */
	public UserCapacityBean findUserCapacity(String userid);
	/**
	 * 获取分配容量历史记录
	 * @param userid
	 * @return
	 */
	public PageInfo<UserCapacityHistoryBean> findHistory(Integer page,Integer limit,String userid);
	/**
	 * 新增用户容量
	 * @param userid
	 * @param totalcapacity
	 * @param capacityunit
	 * @param createuserid
	 * @param createusername
	 */
	public void addUserCapacity(String userid,Long totalcapacity,String capacityunit,String createuserid,String createusername);
	/**
	 * 更新容量
	 * @param type 0（新增已用容量，减少总容量），1（减少已用容量，新增总容量）
	 * @param capacity
	 * @param userid
	 * @param username
	 * @param remark
	 */
	public void updateUserCapacity(int type,long capacity,String userid,String username,String remark);
	
	/**
	 * 初始化（个人登录自动初始化）
	 * @param userid
	 * @param username
	 */
	public void init(String userid,String username);
	/**
	 * 删除用户容量
	 * @param userid
	 */
	public void deleteByUserid(String userid);
	
	
}

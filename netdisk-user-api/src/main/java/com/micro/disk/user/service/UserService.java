package com.micro.disk.user.service;

import java.util.List;
import com.micro.disk.user.bean.Page;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.bean.TreeJson;
import com.micro.disk.user.bean.UserBean;

public interface UserService {
	/**
	 * 用户列表：分页
	 * @param page
	 * @param limit
	 * @param username
	 * @param nickname
	 * @return
	 */
	public Page<UserBean> findUserPageList(Integer page,Integer limit,String username,String nickname);
	/**
	 * 用户树
	 * @param pid 父节点id
	 * @param type 节点类型：org,position,user
	 * @return
	 */
	public List<TreeJson> findUserTree(String pid,String type);
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	public SessionUserBean login(String username, String password);
	/**
	 * 根据token获取用户信息
	 * @param token
	 * @return
	 */
	public SessionUserBean getUserByToken(String token);
	/**
	 * 登出
	 * @param token
	 */
	public void logout(String token);
}

package com.micro.service;

import java.util.List;

import com.micro.model.LogInfo;
import com.micro.model.PageInfo;


public interface LogService {
	/**
	 * 日志追踪-分页列表
	 * @param page
	 * @param limit
	 * @param projectname
	 * @param targetmethod
	 * @param username
	 * @param starttime
	 * @param endtime
	 * @param orderfield
	 * @param ordertype
	 * @return
	 */
	public PageInfo<LogInfo> findList(Integer page,Integer limit,String projectname,String targetmethod,String username,String starttime,String endtime,String orderfield,String ordertype);
	/**
	 * 日志追踪-查看某个日志的链路
	 * @param traceid
	 * @return
	 */
	public List<LogInfo> findLogDetail(String traceid);
	/**
	 * 保存日志
	 * @param bean
	 */
	public void save(LogInfo bean);
}

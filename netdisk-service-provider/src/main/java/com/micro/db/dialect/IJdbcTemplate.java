package com.micro.db.dialect;

import java.util.List;
import java.util.Map;

import com.micro.disk.bean.PageInfo;
public interface IJdbcTemplate {
	
	/**
	 * 添加、修改、删除
	 * @param sql
	 * @param args
	 */
	public void update(String sql,Object[] args);
	
	/**
	 * 数量
	 * @param sql
	 * @param args
	 * @return
	 */
	public Integer findCount(String sql,Object[] args);
	
	/**
	 * 单条数据
	 * @param sql
	 * @param args
	 * @return
	 */
	public Map<String,Object> findOne(String sql,Object[] args);
	/**
	 * 集合
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<Map<String,Object>> findList(String sql,Object[] args);
	/**
	 * 分页
	 * @param sql
	 * @param args
	 * @param page
	 * @param limit
	 * @return
	 */
	public PageInfo<Map<String,Object>> findPageList(String sql,Object[] args,Integer page,Integer limit);
}

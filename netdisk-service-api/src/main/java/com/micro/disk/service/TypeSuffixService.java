package com.micro.disk.service;

import java.util.List;

import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.TypeSuffixBean;
import com.micro.disk.bean.TypeSuffixOperateBean;

public interface TypeSuffixService {
	/**
	 * 新增
	 * @param typecode
	 * @param name
	 * @param icon
	 * @param iconbig
	 * @param suffix
	 * @param operatecodes
	 */
	public void save(String typecode,String name,String icon,String iconbig,String suffix,List<String> operatecodes);
	/**
	 * 修改
	 * @param id
	 * @param name
	 * @param icon
	 * @param iconbig
	 * @param suffix
	 * @param operatecodes
	 */
	public void update(String id,String name,String icon,String iconbig,String suffix,List<String> operatecodes);
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	/**
	 * 查询单条记录
	 * @param id
	 * @return
	 */
	public TypeSuffixBean findOne(String id);
	/**
	 * 查询所有记录（分页）
	 * @return
	 */
	public PageInfo<TypeSuffixBean> findList(Integer page,Integer limit,String typecode);
	/**
	 * 查询所有记录（不分页）
	 * @param typecode
	 * @return
	 */
	public List<TypeSuffixBean> findList(String typecode);
	
	/**
	 * 根据格式查询单条：图标和类型
	 * @param suffix
	 * @return
	 */
	public TypeSuffixBean findBySuffix(String suffix);
	
	/**
	 * 根据后缀查询关联组件
	 * @param suffix
	 * @return
	 */
	public List<TypeSuffixOperateBean> findComponentsBySuffix(String suffix);
	
	/**
	 * 是否支持某种格式上传
	 * @param suffix
	 * @return
	 */
	public boolean isSupportSuffix(String suffix);
}

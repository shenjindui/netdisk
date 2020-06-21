package com.micro.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author 郑伟业
 * 分页实体
 *
 */
@Getter
@Setter
public class PageInfo<T> implements Serializable{
	public final static Integer SUCCESS=0;
	public final static Integer ERROR=1;
	
	//状态
	private Integer code;
	private String msg;
	
	//返回数据
	private List<T> rows;//记录列表
	private long totalPage;//总页数
	private long totalElements;//总记录数
	
	
	//参数接受
    private Integer limit;//每页几条记录（从1开始）
    private Integer page;//当前页数
}

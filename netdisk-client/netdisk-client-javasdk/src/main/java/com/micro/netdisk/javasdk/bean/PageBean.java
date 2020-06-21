package com.micro.netdisk.javasdk.bean;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable{
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
    
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public long getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return "PageBean [code=" + code + ", msg=" + msg + ", rows=" + rows + ", totalPage=" + totalPage
				+ ", totalElements=" + totalElements + ", limit=" + limit + ", page=" + page + "]";
	}
    
    
}

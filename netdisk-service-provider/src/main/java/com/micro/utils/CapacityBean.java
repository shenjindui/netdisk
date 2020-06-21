package com.micro.utils;

public class CapacityBean {
	private Long totalsize;

	public CapacityBean(Long totalsize){
		this.totalsize=totalsize;
	}
	
	public Long getTotalsize() {
		return totalsize;
	}
	public void setTotalsize(Long totalsize) {
		this.totalsize = totalsize;
	}
}

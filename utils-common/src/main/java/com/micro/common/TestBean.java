package com.micro.common;

import java.util.Date;

import lombok.Data;

@Data
public class TestBean {
	private String name;
	private Date starttime;
	@Override
	public String toString() {
		return "TestBean [name=" + name + ", starttime=" + starttime + "]";
	}
}

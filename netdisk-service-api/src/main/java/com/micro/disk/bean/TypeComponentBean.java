package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class TypeComponentBean implements Serializable{
	private String id;
	private String code;
	private String name;
	private String remark;
	private String createuserid;
	private String createusername;
	private String createtime;
}

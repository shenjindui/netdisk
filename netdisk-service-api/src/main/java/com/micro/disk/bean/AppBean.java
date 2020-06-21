package com.micro.disk.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class AppBean implements Serializable{
	private String id;
	private String appname;
	private String appdesc;
	private String createuserid;
	private String createusername;
	private String createtime;
	private Integer delstatus;
}

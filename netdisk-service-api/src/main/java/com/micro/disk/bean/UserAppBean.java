package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserAppBean implements Serializable{
	private String id;
	
	private String appid;
	private String appname;
	
	private String userid;
}

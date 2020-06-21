package com.micro.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable{
	private String id;
	private String nickname;
	private String username;
	private String telephone;
	private String roleid;
	private String rolename;
	private String positionname;
	private String createtimes;
	
	private String totalcapacity;
	private String usedcapacity;
}

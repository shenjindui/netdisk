package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class ShareFriendsBean implements Serializable{
	private String userid;
	private String username;
	
	private String zcstatus;//转存状态
	private String zctime;//转存时间
}

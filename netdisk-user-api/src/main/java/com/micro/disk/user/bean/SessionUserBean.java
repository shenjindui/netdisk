package com.micro.disk.user.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class SessionUserBean implements Serializable{
	private String id;
	private String nickname;
	private String username;
	private String telephone;
	private String token;
}

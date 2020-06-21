package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserBean implements Serializable{
	private String id;
	private String nickname;
	private String username;
}

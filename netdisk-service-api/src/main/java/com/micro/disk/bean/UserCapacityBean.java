package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserCapacityBean implements Serializable{
	private String id;
	private String userid;
	
	private Long totalcapacity;
	private String totalcapacityname;
	
	private Long usedcapacity;
	private String usedcapacityname;
}

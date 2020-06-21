package com.micro.disk.bean;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserCapacityHistoryBean implements Serializable{
	private String capacity;
	private String leftcapacity;
	private String username;
	private String createtime;
	private String remark;
}

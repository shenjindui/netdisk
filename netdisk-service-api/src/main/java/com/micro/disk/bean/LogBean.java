package com.micro.disk.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class LogBean implements Serializable{
	private String id;
	private String requestid;
	private String requestip;
	private String username;
	
	private String projectname;
	private String targetmethod;
	private String targetparams;
	private String starttime;
	private String endtime;
	private Long comsumetime;//耗时
	private String returnresult;//返回结果
	private String executeresult;//执行结果
	////////////////////
	private String olddata;
	private String type;//0-controller,1-service
}

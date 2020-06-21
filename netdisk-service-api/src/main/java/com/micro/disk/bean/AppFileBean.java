package com.micro.disk.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class AppFileBean implements Serializable {
	private String id;
	private String appname;
	private String appid;
	private String businessid;
	private String businesstype;
	private String fileicon;
	private String filename;
	private long filesize;
	private String filesuffix;
	private String typecode;
	private String filemd5;
	private String createuserid;
	private String createusername;
	private String createtime;
	private Integer isbreak;//0正常,1为损坏
}

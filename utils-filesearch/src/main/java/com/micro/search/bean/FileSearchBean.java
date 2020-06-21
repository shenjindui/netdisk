package com.micro.search.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class FileSearchBean implements Serializable{
	private String id;
	private String filename;//查询关键字
	private String pid;
	private String pname;
	
	private String filemd5;
	private String fileicon;
	private String typecode;
	private String filesuffix;
	private String filesize;
	private String filetype;//0文件夹，1文件
	
	private String createuserid;
	private String createusername;
	private String createtime;
	
}

package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class ShareFileBean implements Serializable{
	private String id;
	private String filename;
	private String fileicon;
	private String filesize;
	private Integer filetype;//0文件夹，1文件
	private String createtime;
}

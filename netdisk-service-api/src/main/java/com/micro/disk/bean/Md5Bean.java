package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class Md5Bean implements Serializable{
	private String id;
	private String md5;
	private long filenum;//切块数量
	
	private String filename;
	private String fileicon;
	
	private long filesize;
	private String filesizename;
	
	private String typecode;
	private String filesuffix;
	
	private String thumbnailurl;//图片属性：缩略图
	private String imgsize;//图片属性：尺寸
}

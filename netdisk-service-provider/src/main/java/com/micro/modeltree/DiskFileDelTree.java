package com.micro.modeltree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DiskFileDelTree {
	private String id;
	private String pid;
	private String filename;
	private long filesize;
	private String filesuffix;
	private String typecode;//document/picture/video/music/other
	private String filemd5;
	private Integer filetype;//0文件夹，1文件
	private String createuserid;
	private String createusername;
	private Date createtime;//上传时间
	private Date deletetime;//删除时间
	private String thumbnailurl;//图片属性：缩略图
	private String imgsize;//图片属性：尺寸
	private List<DiskFileDelTree> children=new ArrayList<DiskFileDelTree>();
}

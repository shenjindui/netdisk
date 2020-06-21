package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class MergeFileBean implements Serializable{
	private String userid; //用户id
	private String username;
	private String pid; //文件pid（上传到哪个文件夹下）
	private String uuid; //前端上传uuid
	private String fileid; //前端上传的文件id
	private String filemd5; //文件md5
	private String filename; //文件名称
	private long totalSize; //文件大小
	private String albumid; //相册【*】
	private String relativepath; //文件夹上传（路径）【*】
}

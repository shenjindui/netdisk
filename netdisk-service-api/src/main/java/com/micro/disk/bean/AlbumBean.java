package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class AlbumBean implements Serializable{
	private String id;
	private String albumname;
	private String albumdesc;
	private String coverurl;
	private String createtime;
	private String count;
}

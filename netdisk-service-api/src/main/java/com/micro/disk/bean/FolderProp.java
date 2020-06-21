package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class FolderProp implements Serializable{
	private Integer filenum;
	private Integer foldernum;
	private Long totalsize;
	private String totalsizename;
}

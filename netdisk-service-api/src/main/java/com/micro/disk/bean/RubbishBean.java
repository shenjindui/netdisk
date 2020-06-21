package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class RubbishBean implements Serializable{
	private String id;
	private String fileicon;
	private String filename;
	private String filesize;
	private String deletetime;
	private String remainday;
}

package com.micro.config;

import java.io.Serializable;

import lombok.Data;

@Data
public class RedisChunkTemp implements Serializable{
	//private String pid;
	private String id;
	private String name;
	
	private Long size;//总大小
	private Integer chunks;//总切块数
	private Integer currentsize;//当前切块大小
	private Integer chunk;//第几块
	
	private String userid;
	private String storepath;//存储目录
	private String typecode;
	private String filesuffix;
	private String relativepath;
}

package com.micro.disk.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class NoticeBean implements Serializable{
	private String id;
	private String type;//通知类型
	private String typename;
	private String content;//通知内容
	private String userid;
	private String username;
	private String createtime;
	private String status;//0未阅读，1已阅读
	private String readtime;//阅读时间
}

package com.micro.netdisk.javasdk.bean;

import java.io.Serializable;

public class Result implements Serializable{
	private Integer code;//响应编码（0成功,1失败）
	private String msg;//响应信息（如：添加成功）
	private Object data;//
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}

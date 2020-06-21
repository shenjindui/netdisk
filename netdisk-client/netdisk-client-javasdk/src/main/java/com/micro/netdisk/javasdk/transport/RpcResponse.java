package com.micro.netdisk.javasdk.transport;

import java.io.Serializable;

public class RpcResponse implements Serializable{
	private int code;//0成功，1失败
	private String msg;
	private byte[] data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
}

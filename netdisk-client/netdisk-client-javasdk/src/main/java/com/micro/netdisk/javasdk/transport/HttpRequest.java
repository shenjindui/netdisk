package com.micro.netdisk.javasdk.transport;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends RpcRequest implements Serializable{
	private String requrl;
	private Map<String,String> param=new HashMap<String,String>();
	
	private String receiveName;
	private String fileName;
	private byte[] bytes;
	
	
	
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getRequrl() {
		return requrl;
	}
	public void setRequrl(String requrl) {
		this.requrl = requrl;
	}
	public Map<String, String> getParam() {
		return param;
	}
	public void setParam(Map<String, String> param) {
		this.param = param;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	
}

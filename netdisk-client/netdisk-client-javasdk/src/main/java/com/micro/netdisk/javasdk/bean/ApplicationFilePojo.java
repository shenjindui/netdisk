package com.micro.netdisk.javasdk.bean;

import java.io.Serializable;

public class ApplicationFilePojo implements Serializable{
	private String id;
	private String appname;
	private String appid;
	private String businessid;
	private String businesstype;
	private String filename;
	private long filesize;
	private String filesuffix;
	private String typecode;
	private String filemd5;
	private String createuserid;
	private String createusername;
	private String createtime;
	private Integer isbreak;//0正常,1为损坏
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getBusinessid() {
		return businessid;
	}
	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	public String getFilesuffix() {
		return filesuffix;
	}
	public void setFilesuffix(String filesuffix) {
		this.filesuffix = filesuffix;
	}
	public String getTypecode() {
		return typecode;
	}
	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}
	public String getFilemd5() {
		return filemd5;
	}
	public void setFilemd5(String filemd5) {
		this.filemd5 = filemd5;
	}
	public String getCreateuserid() {
		return createuserid;
	}
	public void setCreateuserid(String createuserid) {
		this.createuserid = createuserid;
	}
	public String getCreateusername() {
		return createusername;
	}
	public void setCreateusername(String createusername) {
		this.createusername = createusername;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public Integer getIsbreak() {
		return isbreak;
	}
	public void setIsbreak(Integer isbreak) {
		this.isbreak = isbreak;
	}
	@Override
	public String toString() {
		return "ApplicationFilePojo [id=" + id + ", appname=" + appname + ", appid=" + appid + ", businessid="
				+ businessid + ", businesstype=" + businesstype + ", filename=" + filename + ", filesize=" + filesize
				+ ", filesuffix=" + filesuffix + ", typecode=" + typecode + ", filemd5=" + filemd5 + ", createuserid="
				+ createuserid + ", createusername=" + createusername + ", createtime=" + createtime + ", isbreak="
				+ isbreak + "]";
	}
	
	
	
}

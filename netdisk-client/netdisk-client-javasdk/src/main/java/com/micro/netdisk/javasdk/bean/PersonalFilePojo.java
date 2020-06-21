package com.micro.netdisk.javasdk.bean;

import java.io.Serializable;

public class PersonalFilePojo implements Serializable{
	private String id;
	private String pid;
	private String pname;
	private String filename;
	private String filesize;
	private String filesuffix;
	private String fileicon;//base64
	private String typecode;//document/picture/video/music/other
	private String filemd5;
	private Integer filetype;//0文件夹，1文件
	private String createuserid;
	private String createusername;
	private String createtime;
	private String thumbnailurl;//图片属性：缩略图
	private String imgsize;//图片属性：尺寸
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getFilesuffix() {
		return filesuffix;
	}
	public void setFilesuffix(String filesuffix) {
		this.filesuffix = filesuffix;
	}
	public String getFileicon() {
		return fileicon;
	}
	public void setFileicon(String fileicon) {
		this.fileicon = fileicon;
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
	public Integer getFiletype() {
		return filetype;
	}
	public void setFiletype(Integer filetype) {
		this.filetype = filetype;
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
	public String getThumbnailurl() {
		return thumbnailurl;
	}
	public void setThumbnailurl(String thumbnailurl) {
		this.thumbnailurl = thumbnailurl;
	}
	public String getImgsize() {
		return imgsize;
	}
	public void setImgsize(String imgsize) {
		this.imgsize = imgsize;
	}
	@Override
	public String toString() {
		return "PersonalFilePojo [id=" + id + ", pid=" + pid + ", pname=" + pname + ", filename=" + filename
				+ ", filesize=" + filesize + ", filesuffix=" + filesuffix + ", fileicon=" + fileicon + ", typecode="
				+ typecode + ", filemd5=" + filemd5 + ", filetype=" + filetype + ", createuserid=" + createuserid
				+ ", createusername=" + createusername + ", createtime=" + createtime + ", thumbnailurl=" + thumbnailurl
				+ ", imgsize=" + imgsize + "]";
	}
	
	
}

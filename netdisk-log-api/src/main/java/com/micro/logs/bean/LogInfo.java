package com.micro.logs.bean;

import java.io.Serializable;
import java.util.Date;

public class LogInfo implements Serializable{
	private String traceid;
	private String requestip;
	
	private String userid;
	private String username;
	
	private String projectname;
	private String projectdesc;
	
	private String targetmethod;
	private String targetparams;
	
	private String starttime;
	private String endtime;
	private Long comsumetime;//耗时
	
	private String returnresult;//返回结果
	private String executeresult;//执行结果
	
	private String remark;

	
	
	
	

	@Override
	public String toString() {
		return "LogInfo [traceid=" + traceid + ", requestip=" + requestip + ", userid=" + userid + ", username="
				+ username + ", projectname=" + projectname + ", projectdesc=" + projectdesc + ", targetmethod="
				+ targetmethod + ", targetparams=" + targetparams + ", starttime=" + starttime + ", endtime=" + endtime
				+ ", comsumetime=" + comsumetime + ", returnresult=" + returnresult + ", executeresult=" + executeresult
				+ ", remark=" + remark + "]";
	}

	public String getTraceid() {
		return traceid;
	}

	public void setTraceid(String traceid) {
		this.traceid = traceid;
	}

	public String getRequestip() {
		return requestip;
	}

	public void setRequestip(String requestip) {
		this.requestip = requestip;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getProjectdesc() {
		return projectdesc;
	}

	public void setProjectdesc(String projectdesc) {
		this.projectdesc = projectdesc;
	}


	public String getTargetmethod() {
		return targetmethod;
	}

	public void setTargetmethod(String targetmethod) {
		this.targetmethod = targetmethod;
	}

	public String getTargetparams() {
		return targetparams;
	}

	public void setTargetparams(String targetparams) {
		this.targetparams = targetparams;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Long getComsumetime() {
		return comsumetime;
	}

	public void setComsumetime(Long comsumetime) {
		this.comsumetime = comsumetime;
	}

	public String getReturnresult() {
		return returnresult;
	}

	public void setReturnresult(String returnresult) {
		this.returnresult = returnresult;
	}

	public String getExecuteresult() {
		return executeresult;
	}

	public void setExecuteresult(String executeresult) {
		this.executeresult = executeresult;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	
	
	
	
	
}

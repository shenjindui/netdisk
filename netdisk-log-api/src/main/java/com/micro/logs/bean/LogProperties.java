package com.micro.logs.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "busilog")
@Component
public class LogProperties {
	private String projectname;
	private String projectdesc;
	private String host;
	private String state="on";//on,off
	
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
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}

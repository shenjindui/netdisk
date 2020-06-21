package com.micro.netdisk.javasdk.demo;

public class TestBean {
	private volatile boolean iserror=false;

	public boolean isIserror() {
		return iserror;
	}

	public void setIserror(boolean iserror) {
		this.iserror = iserror;
	}
	
}

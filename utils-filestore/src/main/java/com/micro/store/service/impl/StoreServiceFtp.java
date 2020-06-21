package com.micro.store.service.impl;

import com.micro.store.service.StoreService;

public class StoreServiceFtp implements StoreService{
	private String enable;
	
	
	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Override
	public String upload(String group, byte[] bytes, String fileName) {
		System.out.println("ftp.............................................................");
		return null;
	}

	@Override
	public byte[] download(String group,String path) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public byte[] download(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mkdir(String folders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listFiles(String folder) {
		// TODO Auto-generated method stub
		
	}

}

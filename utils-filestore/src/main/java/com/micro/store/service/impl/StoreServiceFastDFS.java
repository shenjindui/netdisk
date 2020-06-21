package com.micro.store.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.micro.store.service.StoreService;

@Component(value="FastDFS")
public class StoreServiceFastDFS implements StoreService{
	@Autowired
	private FastFileStorageClient storageClient;

	@Override
	public String upload(String group, byte[] bytes, String fileName) {
		String fileExtName = FilenameUtils.getExtension(fileName);
	    StorePath sp=storageClient.uploadFile(group, bytes, fileExtName);
	    return sp.getFullPath();
	}

	@Override
	public byte[] download(String group,String path) {
		byte[] bytes=storageClient.downloadFile(group,path);
		return bytes;
	}
	
	@Override
	public byte[] download(String path) {
		byte[] bytes=storageClient.downloadFile("",path);
		return bytes;
	}

	@Override
	public void delete(String path) {
		storageClient.deleteFile(path);
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

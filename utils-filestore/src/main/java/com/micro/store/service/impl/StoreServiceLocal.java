package com.micro.store.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.store.service.StoreService;
import com.micro.store.utils.FileUtils;

@Component(value="Local")
public class StoreServiceLocal implements StoreService{
	@NacosValue("${uploadlocalpath}")
	private String path;

	@Override
	public String upload(String group,byte[] bytes, String fileName) {
		String folders=FileUtils.getFolder();
		String newName=UUID.randomUUID().toString()+"."+FilenameUtils.getExtension(fileName);
		try{
			//创建文件夹
			File fileFolder = new File(path+"/"+folders);
			if(!fileFolder.exists()){
				fileFolder.mkdirs();
			}
			
			//上传服务器（源文件）
			/*InputStream input = new ByteArrayInputStream(bytes);
			OutputStream output =new FileOutputStream(new File(fileUploadLocalConfig.getPath()+"/"+folders+"/"+fileName));
			IOUtils.copyLarge(input, output);
			output.close();
			input.close();*/
			
			File f=new File(path+"/"+folders+"/"+newName);
			OutputStream output = new FileOutputStream(f);
			InputStream input = new ByteArrayInputStream(bytes);
			IOUtils.copyLarge(input, output);
			output.close();
			input.close();
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return folders+"/"+newName;
	}

	@Override
	public byte[] download(String group,String path) {
		try{
			File file = new File(this.path+"/"+path);
			InputStream input = new FileInputStream(file);
			byte[] bytes = IOUtils.toByteArray(input);
			input.close();
			return bytes;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	@Override
	public byte[] download(String path) {
		try{
			File file = new File(this.path+"/"+path);
			InputStream input = new FileInputStream(file);
			byte[] bytes = IOUtils.toByteArray(input);
			input.close();
			return bytes;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void delete(String path) {
		File file = new File(this.path+"/"+path);
		file.delete();
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

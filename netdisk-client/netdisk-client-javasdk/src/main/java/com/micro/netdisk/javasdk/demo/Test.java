package com.micro.netdisk.javasdk.demo;

import java.util.ArrayList;
import java.util.List;

import com.micro.netdisk.javasdk.balance.HostBean;
import com.micro.netdisk.javasdk.bean.ApplicationFilePojo;
import com.micro.netdisk.javasdk.bean.PageBean;
import com.micro.netdisk.javasdk.bean.PersonalFilePojo;
import com.micro.netdisk.javasdk.factory.FileFactory;
import com.micro.netdisk.javasdk.service.FileService;

public class Test {
	
	public static void main(String[] args) {
		List<HostBean> hosts=new ArrayList<>();
		hosts.add(new HostBean("127.0.0.1", 8015, 1));
		FileService fs=FileFactory.createFileService(hosts);
		
		String fileName="咕泡AI20200109公开课--K-means实现图像分割（Aaron老师）.mp4";
		String fileMd5="e5bc33e8397ff1a41d0c45cd3b5dac0e";
		
		//String fileName="青山_20200201_分布式架构核心技术之消息队列.mp4";
		//String fileMd5="5d2cf21a8e772d185a4b3739762619b8";
		
		String appId="1111";
		String tempFilePath="E:/test/"+fileName;
		String businessId="zwy";
		String businessType="default";
		String userId="1";
		String userName="超级管理员";
		boolean allowMultiple=true;
		
		PageBean<ApplicationFilePojo> lists = fs.findFilesByAppid(1, 2, appId);
		System.out.println(lists);
	}
	
	
}

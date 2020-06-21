package com.micro.mvc;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.nacos.api.config.annotation.NacosValue;

@Configuration
public class FileSizeConfig {
	@NacosValue(value="${upload.maxsize}",autoRefreshed=true)
	private String maxsize = "100";
	
	@NacosValue(value="${upload.maxrequestsize}",autoRefreshed=true)
	private String maxrequestsize = "100";
	
	@Bean  
	public MultipartConfigElement multipartConfigElement() {  
		MultipartConfigFactory factory = new MultipartConfigFactory();  
		//文件最大  
		factory.setMaxFileSize(maxsize); //KB,MB  
		// 设置总上传数据总大小  
		factory.setMaxRequestSize(maxrequestsize);  
		return factory.createMultipartConfig();  
	} 
}

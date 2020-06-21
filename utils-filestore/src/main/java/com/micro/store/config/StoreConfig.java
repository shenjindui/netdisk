package com.micro.store.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.micro.store.service.StoreService;
import com.micro.store.service.impl.StoreServiceFastDFS;
import com.micro.store.service.impl.StoreServiceFtp;
import com.micro.store.service.impl.StoreServiceHDFS;
import com.micro.store.service.impl.StoreServiceLocal;

/**
 * 主要根据不同的条件让不同的框架生效
 * @author Administrator
 *
 */
//@Configuration
public class StoreConfig {
	@Bean
	@ConditionalOnMissingBean(value=StoreService.class)
	@ConditionalOnProperty(prefix="store",name="type",havingValue="fastdfs")
	@ConfigurationProperties(prefix="fastdfs.store")
	public StoreServiceFastDFS storeFastDFS(){
		StoreServiceFastDFS store=new StoreServiceFastDFS();
		
		return store;
	}
	
	@Bean
	@ConditionalOnMissingBean(value=StoreService.class)
	@ConditionalOnProperty(prefix="store",name="type",havingValue="hdfs")
	@ConfigurationProperties(prefix="hdfs.store")
	public StoreServiceHDFS storeHDFS(){
		StoreServiceHDFS store=new StoreServiceHDFS();
		return store;
	}
	
	@Bean
	@ConditionalOnMissingBean(value=StoreService.class)
	@ConditionalOnProperty(prefix="store",name="type",havingValue="ftp")
	@ConfigurationProperties(prefix="ftp.store")
	public StoreServiceFtp storeFtp(){
		StoreServiceFtp store=new StoreServiceFtp();
		return store;
	}
	
	@Bean
	@ConditionalOnMissingBean(value=StoreService.class)
	@ConditionalOnProperty(prefix="store",name="type",havingValue="local")
	@ConfigurationProperties(prefix="local.store")
	public StoreServiceLocal storeLocal(){
		StoreServiceLocal local=new StoreServiceLocal();
		return local;
	}
}

package com.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

@SpringBootApplication
@EnableDubboConfiguration
@NacosPropertySource(dataId = "netdisk-user-provider",groupId="netdisk",autoRefreshed=true)
public class UserProviderApp {
	
	public static void main(String[] args) {
		SpringApplication.run(UserProviderApp.class, args);
	}
}

//排除内置Tomcat
/*@SpringBootApplication
@EnableDubboConfiguration
@NacosPropertySource(dataId = "netdisk-user-provider",groupId="netdisk",autoRefreshed=true)
public class UserProviderApp extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(UserProviderApp.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(UserProviderApp.class, args);
	}
}*/
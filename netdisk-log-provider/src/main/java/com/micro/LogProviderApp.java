package com.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

@SpringBootApplication
@NacosPropertySource(dataId = "netdisk-log-provider",groupId="netdisk",autoRefreshed=true)
public class LogProviderApp{
	
	public static void main(String[] args) {
		SpringApplication.run(LogProviderApp.class, args);
	}
}

//排除内置Tomcat
/*@SpringBootApplication
@NacosPropertySource(dataId = "netdisk-log-provider",groupId="netdisk",autoRefreshed=true)
public class LogProviderApp extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LogProviderApp.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LogProviderApp.class, args);
	}
}
*/
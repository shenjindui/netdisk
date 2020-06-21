package com.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

@SpringBootApplication
@EnableDubboConfiguration
@NacosPropertySource(dataId = "netdisk-client-provider",groupId="netdisk",autoRefreshed=true)
public class ClientProviderApp {
	
	public static void main(String[] args) {
		SpringApplication.run(ClientProviderApp.class, args);
	}
}
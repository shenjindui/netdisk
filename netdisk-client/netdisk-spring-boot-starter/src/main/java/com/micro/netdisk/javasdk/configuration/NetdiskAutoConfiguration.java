package com.micro.netdisk.javasdk.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.micro.netdisk.javasdk.balance.HostBean;
import com.micro.netdisk.javasdk.factory.FileFactory;
import com.micro.netdisk.javasdk.service.FileService;

@Configuration
@EnableConfigurationProperties(NetdiskProperties.class) 
public class NetdiskAutoConfiguration {
	@Autowired
    private NetdiskProperties netdiskProperties;
    
    @Bean
    public FileService fileService(){
    	String host=netdiskProperties.getHost();
    	if(StringUtils.isEmpty(host)){
    		throw new RuntimeException("请在配置文件配置netdisk.server.host=xxx");
    	}
    	List<HostBean> beans=new ArrayList<>();
    	String[] hosts=host.split(",");
    	for(String h:hosts){
    		String ip=h.split(":")[0];
    		int port=Integer.parseInt(h.split(":")[1]);
    		HostBean hb=new HostBean(ip, port, 1);
    		beans.add(hb);
    	}
        FileService fs=FileFactory.createFileService(beans);
        return fs;
    }
}

package com.micro.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * spring mvc的扩展配置
 * @author zwy
 * 2018年10月16日
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).maxAge(3600).allowedMethods("GET", "POST","OPTIONS");
	}

}

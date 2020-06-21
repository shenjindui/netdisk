package com.micro.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * spring mvc的扩展配置
 * @author zwy
 * 2018年10月16日
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{
	@Autowired
	private LimitInterceptor limitInterceptor;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).maxAge(3600).allowedMethods("GET", "POST","OPTIONS");
	}

	/**
	 * app拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(limitInterceptor)
			.addPathPatterns("/**")
			//切块上传
			.excludePathPatterns("/disk/fileupload/uploadChunk")
			
			//登录、登出
			.excludePathPatterns("/security/**")
			
			//分享提取
			.excludePathPatterns("/disk/shareextract/findShareInfo") 
			.excludePathPatterns("/disk/shareextract/validateCode")
			.excludePathPatterns("/disk/shareextract/findShareList")
		
			//swagger
			.excludePathPatterns("/doc.html")
			.excludePathPatterns("/api-docs-ext")
			.excludePathPatterns("/swagger-resources")
			.excludePathPatterns("/v2/**")
			.excludePathPatterns("/api-docs")
			.excludePathPatterns("/swagger-ui.html")
			.excludePathPatterns("/swagger-resources/configuration/ui")
			.excludePathPatterns("/swagger-resources/configuration/security");
		super.addInterceptors(registry);
	}

	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}*/
	
	
}

package com.micro.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.disk.service.TypeSuffixService;

/**
 * 文件校验，控件文件大小
 * @author Administrator
 *
 */
@Aspect
@Component
public class FileCheckAop {
	@Pointcut("execution(* com.micro.controller.FileUploadController.checkFile(..))")
	private void pointcut(){}
	
	@NacosValue(value="${upload.size}",autoRefreshed=true)
	private long size;
	
	@NacosValue(value="${upload.maxsize}",autoRefreshed=true)
	private String maxsize;
	
	@Reference(check=false)
	private TypeSuffixService typeSuffixService;
	
	@Before("pointcut()")
	public void beforeDownload(JoinPoint jp){
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response=attributes.getResponse();
		HttpServletRequest request = attributes.getRequest();
		
		String fileSize=request.getParameter("filesize");
		String filename=request.getParameter("filename");
		
		String suffix=FilenameUtils.getExtension(filename);
		boolean result=typeSuffixService.isSupportSuffix(suffix);
		if(!result){
			throw new RuntimeException("网盘暂时不支持该格式,请联系管理员添加!"+suffix);
		}
		
		if(!StringUtils.isEmpty(fileSize)){
			long filesize=Long.parseLong(fileSize);
			if(filesize>size){
				//方式1：会执行mergeFile
				//throw new RuntimeException("单文件上传,最大不能超过"+size);
				
				//方式2：不会执行mergeFile
				//response.setStatus(500);
				throw new RuntimeException("单文件上传,最大不能超过"+maxsize);
			}
		}
	}
}

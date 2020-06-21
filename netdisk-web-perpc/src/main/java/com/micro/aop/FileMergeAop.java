package com.micro.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.nacos.api.config.annotation.NacosValue;

/**
 * 切块合并，控制文件大小
 * @author Administrator
 *
 */
@Aspect
@Component
public class FileMergeAop {
	@Pointcut("execution(* com.micro.controller.FileUploadController.mergeChunk(..))")
	private void pointcut(){}
	
	@NacosValue(value="${upload.size}",autoRefreshed=true)
	private long size;
	
	@NacosValue(value="${upload.maxsize}",autoRefreshed=true)
	private String maxsize;
	
	@Before("pointcut()")
	public void beforeDownload(JoinPoint jp){
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String totalSize=request.getParameter("totalSize");
		if(!StringUtils.isEmpty(totalSize)){
			long filesize=Long.parseLong(totalSize);
			if(filesize>size){
				throw new RuntimeException("单文件上传,最大不能超过"+maxsize);
			}
		}
	}
}

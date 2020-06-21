package com.micro.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.disk.bean.FileBean;
import com.micro.disk.service.FileService;

/**
 * 文件预览时，控制是否允许预览，主要是阿里云配置太低，如果预览则容易导致宕机
 * @author Administrator
 *
 */
@Aspect
@Component
public class AllowPreviewAop {
	@Pointcut("execution(* com.micro.controller.FileOpenController.writeByte(..))")
	private void pointcut(){}
	
	@NacosValue(value="${allowpreview}",autoRefreshed=true)
	private boolean allowpreview;
	
	@NacosValue(value="${allowsuffix}",autoRefreshed=true)
	private String allowsuffix;
	
	@Reference(check = false)
	private FileService fileService;
	
	@Before("pointcut()")
	public void before(JoinPoint jp){
		if(!allowpreview){
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			String fileid=request.getParameter("fileid");
			FileBean fb=fileService.findOne(fileid);
			if(fb==null){
				throw new RuntimeException("文件ID不存在");
			}
			String filesuffix=fb.getFilesuffix();
			if(!StringUtils.isEmpty(allowsuffix)){
				String[] suffixs=allowsuffix.split(",");
				boolean isallow=false;
				for(String suffix:suffixs){
					if(filesuffix.equals(suffix)){
						isallow=true;
						break;
					}
				}
				if(!isallow){					
					throw new RuntimeException("演示环境,不支持"+filesuffix+"格式的文件预览!");
				}
			}
			
		}
	}
}

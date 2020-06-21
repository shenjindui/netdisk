package com.micro.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.alibaba.nacos.api.config.annotation.NacosValue;

/**
 * 文件下载时，控制是否允许下载，主要是阿里云配置太低，如果下载则容易导致宕机
 * @author Administrator
 *
 */
@Aspect
@Component
public class AllowDownloadAop {
	@Pointcut("execution(* com.micro.controller.FileDownloadController.download(..))")
	private void pointcut1(){}
	
	@Pointcut("execution(* com.micro.controller.FileDownloadController.mergeFiles(..))")
	private void pointcut2(){}
	
	@NacosValue(value="${allowdownload}",autoRefreshed=true)
	private boolean allowdownload;
	
	@Pointcut("pointcut1() || pointcut2()")
	private void pointcut(){}
	
	@Before("pointcut()")
	public void before(JoinPoint jp){
		if(!allowdownload){
			throw new RuntimeException("演示环境,不允许进行文件下载!");
		}
	}
}

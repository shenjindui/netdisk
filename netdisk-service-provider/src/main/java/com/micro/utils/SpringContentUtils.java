package com.micro.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.micro.chain.core.Handler;

@Component
public class SpringContentUtils implements ApplicationContextAware{
	public static ApplicationContext applicationContext = null;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(SpringContentUtils.applicationContext == null){
			SpringContentUtils.applicationContext  = applicationContext;
		}
	}
	
	public Handler getHandler(Class clazz){
		return (Handler) applicationContext.getBean(clazz, null);
	}
	
	//通过name获取 Bean.
	public Object getBean(String name){
		try{			
			return applicationContext.getBean(name);
		}catch(Exception e){
			return null;
		}
	}
}

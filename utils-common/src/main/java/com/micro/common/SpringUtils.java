package com.micro.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

//@Component
public class SpringUtils implements ApplicationContextAware{
	public static ApplicationContext applicationContext = null;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(SpringUtils.applicationContext == null){
			SpringUtils.applicationContext  = applicationContext;
		}
	}
	
	//通过name获取 Bean.
	public Object getBean(String name){
		try{
			//String[] list = applicationContext.getBeanDefinitionNames();
			return applicationContext.getBean(name);
		}catch(Exception e){
			return null;
		}
	}
}

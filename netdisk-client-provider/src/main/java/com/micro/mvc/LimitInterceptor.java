package com.micro.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.disk.service.AppService;

/**
 * app拦截器
 * @author zwy
 * 2018年10月16日
 */
@Component
public class LimitInterceptor implements HandlerInterceptor{
	@Reference(check=false)
	private AppService appService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		String origin = request.getHeader("Origin");
		response.setHeader("Access-Control-Allow-Origin", origin);
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers","Origin,Content-Type,Accept,token,X-Requested-With");
		response.setHeader("Access-Control-Allow-Credentials", "true");
	
		try{
			//校验APPID是否正确
			String appId=request.getParameter("appId");
			if(StringUtils.isEmpty(appId)){
				throw new RuntimeException("appId不能为空");
			}
			boolean flag=appService.checkAppID(appId);
			if(!flag){
				throw new RuntimeException("appId无效,请联系管理员进行注册");
			}
		}catch(Exception e){
			throw new RuntimeException("拦截器异常："+e.getMessage());				
		}
		return true;
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mv)
			throws Exception {
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}
}

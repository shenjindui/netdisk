package com.micro.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;

@RestController
@RequestMapping("/security")
public class LoginController {
	@Reference(check=false)
	private UserService userService;
	
	@RequestMapping("/login")
	public Result login(String username,String password,HttpServletRequest request){
		try{
			//登录
			SessionUserBean bean=userService.login(username, password);
			if(bean!=null){
				//设置用户信息
				request.setAttribute("userid",bean.getId());
				request.setAttribute("username",bean.getNickname());
			}else{
				throw new RuntimeException("用户名或密码错误");
			}
			return ResultUtils.success("认证成功", bean);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	@RequestMapping("/logout")
	public Result logout(String token,HttpServletRequest request){
		try{			
			SessionUserBean ui=userService.getUserByToken(token);
			if(ui!=null){		
				//设置用户信息
				request.setAttribute("userid",ui.getId());
				request.setAttribute("username",ui.getNickname());
				
				//清空token
				userService.logout(token);
			}
			return ResultUtils.success("退出成功", null);
		}catch(Exception e){
			return ResultUtils.success("退出成功", null);
		}
	}
}

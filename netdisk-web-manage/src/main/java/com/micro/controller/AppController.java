package com.micro.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.AppBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.UserBean;
import com.micro.disk.service.AppService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

@RestController
@RequestMapping("/app")
public class AppController {
	@Reference(check=false)
	private AppService appService;
	
	@RequestMapping("/findList")
	public PageInfo<AppBean> findList(PageInfo<AppBean> pi,String name){
		return appService.findPageList(pi.getPage(), pi.getLimit(), name);
	}
	
	@RequestMapping("/findListByAppid")
	public PageInfo<UserBean> findListByAppid(PageInfo<UserBean> pi,String appid,String username){
		return appService.findUserListByAppid(pi.getPage(), pi.getLimit(),appid, username);
	}
	
	@RequestMapping("/findOne")
	public Result findOne(String id){
		try{
			return ResultUtils.success("查询成功", appService.findOne(id));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	@RequestMapping("/save")
	public Result save(AppBean bean,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			bean.setCreateuserid(user.getId());
			bean.setCreateusername(user.getNickname());
			
			appService.save(bean);
			return ResultUtils.success("保存成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@RequestMapping("/update")
	public Result update(AppBean bean){
		try{
			appService.update(bean);
			return ResultUtils.success("修改成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@RequestMapping("/delete")
	public Result delete(String id){
		try{
			appService.delete(id);
			return ResultUtils.success("删除成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

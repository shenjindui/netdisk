package com.micro.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.TypeComponentBean;
import com.micro.disk.service.TypeComponentService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

@RestController
@RequestMapping("/typecomponent")
public class TypeComponentController {
	@Reference(check=false)
	private TypeComponentService typeComponentService;
	
	@RequestMapping("/findList")
	public Result findList(){
		try{
			return ResultUtils.success("查询成功",typeComponentService.findList());
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	@RequestMapping("/findOne")
	public Result findOne(String id){
		try{
			return ResultUtils.success("查询成功",typeComponentService.findOne(id));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	@RequestMapping("/save")
	public Result save(TypeComponentBean bean,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			bean.setCreateuserid(user.getId());
			bean.setCreateusername(user.getNickname());
			typeComponentService.save(bean);
			return ResultUtils.success("新增成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	@RequestMapping("/update")
	public Result update(TypeComponentBean bean){
		try{
			typeComponentService.update(bean);
			return ResultUtils.success("修改成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	@RequestMapping("/delete")
	public Result delete(String id){
		try{
			typeComponentService.delete(id);
			return ResultUtils.success("删除成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

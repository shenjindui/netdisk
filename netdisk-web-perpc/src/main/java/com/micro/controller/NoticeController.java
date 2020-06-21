package com.micro.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.NoticeBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.NoticeService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Api(tags="通知管理")
@RestController
@RequestMapping("/disk/notice")
public class NoticeController {
	@Reference(check=false)
	private NoticeService noticeService;
	
	@ApiOperation(value="通知列表",notes="通知列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "每页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findList")
	public PageInfo<NoticeBean> findList(PageInfo<NoticeBean> pi,HttpServletRequest request){
		SessionUserBean user=UserInfoUtils.getBean(request);
		
		return noticeService.findList(pi.getPage(), pi.getLimit(), user.getId());
	}
	
	@ApiOperation(value="更新通知状态（已阅读）",notes="更新通知状态（已阅读）")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/updateReadStatus")
	public Result updateReadStatus(HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			
			noticeService.updateReadStatus(user.getId());
			return ResultUtils.success("批量更改为已阅读成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="清空通知列表",notes="清空通知列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/delete")
	public Result delete(HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			
			noticeService.delete(user.getId());
			return ResultUtils.success("批量清空通知成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

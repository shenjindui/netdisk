package com.micro.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.ShareBean;
import com.micro.disk.service.FileService;
import com.micro.disk.service.ShareService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Api(tags="好友分享")
@RestController
@RequestMapping("/disk/sharebyfriends")
public class ShareByFriendsController {
	@Reference(check=false)
	private ShareService shareService;
	@Reference(check=false)
	private FileService fileService;
	
	@ApiOperation(value="好友分享列表",notes="好友分享列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "每页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "status", value = "状态（空表示全部，0正常，1已失效，2已撤销）",dataType = "Integer",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findList")
	public PageInfo<ShareBean> findList(PageInfo<ShareBean> pi,Integer status,HttpServletRequest request){
		SessionUserBean user=UserInfoUtils.getBean(request);
		return shareService.findFriendsShare(pi.getPage(), pi.getLimit(), user.getId(), status);
	}
	
	@ApiOperation(value="分享文件列表",notes="分享文件列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "分享id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "pid", value = "文件pid（最顶层则为0）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findShareList")
	public Result findShareList(String id,String pid){
		try{
			return ResultUtils.success("查询成功",shareService.findShareFileListFromFriends(id, pid));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
		
}

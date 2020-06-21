package com.micro.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.ValidateUtils;
import com.micro.disk.bean.FileBean;
import com.micro.disk.service.FileService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="文件在线创建/编辑")
@RestController
@RequestMapping("/disk/fileedit")
public class FileEditController {
	@Reference(check = false)
	private FileService fileService;
	
	@ApiOperation(value="编辑文件",notes="编辑md文件、文本文件等")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "fileid", value = "文件ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "content", value = "文件内容",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/editFile")
	public Result editFile(String fileid,String content){
		try{
			ValidateUtils.validate(fileid, "文件ID");
			ValidateUtils.validate(content, "文件内容");
			
			FileBean fb=fileService.editFile(fileid, content.getBytes());
			return ResultUtils.success("保存成功", fb);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="创建文件",notes="创建md文件、文本文件等")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pid", value = "所属目录ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "filename", value = "文件名称",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "content", value = "文件内容",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/addFile")
	public Result addFile(String pid,String filename,String content,HttpServletRequest request){
		try{
			ValidateUtils.validate(pid, "所属目录ID");
			ValidateUtils.validate(filename, "文件名称");
			ValidateUtils.validate(content, "文件内容");
			
			SessionUserBean user=UserInfoUtils.getBean(request);
			String userid=user.getId();
			String username=user.getNickname();
			FileBean fb=fileService.addFile(pid, filename, content.getBytes(), userid, username);
			return ResultUtils.success("保存成功", fb);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	
	@PostMapping("/editOffice")
	public Result editOffice(){
		try{
			
			return ResultUtils.success("保存成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@PostMapping("/addOffice")
	public Result addOffice(){
		try{
			
			return ResultUtils.success("保存成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

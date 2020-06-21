package com.micro.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.disk.service.FileService;
import com.micro.disk.service.ShareService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="分享文件提取")
@RestController
@RequestMapping("/disk/shareextract")
public class ShareExtractController {
	@Reference(check=false)
	private ShareService shareService;
	@Reference(check=false)
	private FileService fileService;
	
	@ApiOperation(value="查询分享基本信息",notes="查询分享基本信息（不需要登录）")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "分享id",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findShareInfo")
	public Result findShareInfo(String id){
		try{
			
			return ResultUtils.success("验证成功",shareService.findShareInfo(id));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
		
	@ApiOperation(value="验证提取码",notes="验证提取码（不需要登录）")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "分享id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "code", value = "提取码",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/validateCode")
	public Result validateCode(String id,String code){
		try{
			String token=shareService.validateCode(id, code);
			return ResultUtils.success("验证成功",token);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="分享文件列表",notes="分享文件列表（不需要登录）")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "分享id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "pid", value = "文件的pid（最顶层则为0）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "认证提取码成功之后返回token值",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findShareList")
	public Result findShareList(String id,String pid,String token){
		try{
			
			return ResultUtils.success("查询成功",shareService.findShareFileListFromSecret(id, pid,token));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
		
	@ApiOperation(value="分享文件转存",notes="分享文件转存（需要登录）")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "shareid", value = "分享id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "folderid", value = "保存到目标文件夹ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "idjson", value = "勾选文件id（[{'id':'xx'}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/saveFromShare")
	public Result saveFromShare(String shareid,String folderid,String idjson,HttpServletRequest request){
		try{
			if(StringUtils.isEmpty(idjson)){
				throw new RuntimeException("请勾选需要转存的文件");
			}
			//json转换
			List<String> ids=new ArrayList<String>();
			JsonUtils jsonUtils=new JsonJackUtils();
			List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
			lists.forEach(m->{
				ids.add(m.get("id")==null?"":m.get("id").toString());
			});
			
			//user信息
			SessionUserBean user=UserInfoUtils.getBean(request);
			String userid=user.getId();
			String username=user.getNickname();
			
			fileService.saveFromShare(userid, username, folderid, shareid, ids);
			return ResultUtils.success("转存成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

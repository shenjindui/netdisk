package com.micro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.xml.MenuBean;
import com.micro.xml.XstreamUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Api(tags="获取菜单")
@RestController
@RequestMapping("/menu")
public class MenuController {
	
	@ApiOperation(value="查询菜单集合",notes="查询菜单集合")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "roleid", value = "角色ID",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findList")
	public Result findMenus(String roleid){	
		try{
			List<MenuBean> data=XstreamUtils.parseMenuxml();
			return ResultUtils.success("查询成功", data);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

package com.micro.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.RubbishBean;
import com.micro.disk.service.RubbishService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 回收站
 * @author Administrator
 *
 */
@Api(tags="回收站管理")
@RestController
@RequestMapping("/disk/rubbish")
public class RubbishController {
	@Reference(check=false)
	private RubbishService rubbishService;
	
	@ApiOperation(value="回收站列表",notes="回收站列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "每页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findList")
	public PageInfo<RubbishBean> findList(PageInfo<RubbishBean> pi,HttpServletRequest request){
		SessionUserBean user=UserInfoUtils.getBean(request);
		return rubbishService.findPageList(pi.getPage(), pi.getLimit(), user.getId());
	}
	
	@ApiOperation(value="清空选择文件",notes="清空选择文件")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "idjson", value = "勾选文件id（[{'id':'xx'}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/delete")
	public Result delete(String idjson,HttpServletRequest request){
		try{
			if(StringUtils.isEmpty(idjson)){
				throw new RuntimeException("请选择清空记录");
			}
			JsonUtils jsonUtils=new JsonJackUtils();
			List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
			List<String> ids=new ArrayList<String>();
			
			if(!CollectionUtils.isEmpty(lists)){
				for(Map m:lists){
					ids.add(m.get("id")==null?"":m.get("id").toString());
				}
			}
			SessionUserBean user=UserInfoUtils.getBean(request);
			rubbishService.delete(ids,user.getId());
			return ResultUtils.success("清空回收站成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="还原选择文件",notes="还原选择文件")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "folderid", value = "还原到目标文件夹ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "idjson", value = "勾选文件id（[{'id':'xx'}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/recover")
	public Result recover(String folderid,String idjson,HttpServletRequest request){
		try{
			if(StringUtils.isEmpty(idjson)){
				throw new RuntimeException("请选择还原记录");
			}
			JsonUtils jsonUtils=new JsonJackUtils();
			List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
			List<String> ids=new ArrayList<String>();
			
			if(!CollectionUtils.isEmpty(lists)){
				for(Map m:lists){
					ids.add(m.get("id")==null?"":m.get("id").toString());
				}
			}
			
			SessionUserBean user=UserInfoUtils.getBean(request);
			rubbishService.recover(folderid,ids,user.getId(),user.getNickname());
			return ResultUtils.success("还原成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

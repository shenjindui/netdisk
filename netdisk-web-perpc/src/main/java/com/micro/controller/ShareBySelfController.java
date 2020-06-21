package com.micro.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.micro.disk.bean.ShareBean;
import com.micro.disk.service.ShareService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="我的分享")
@RestController
@RequestMapping("/disk/sharebyself")
public class ShareBySelfController {
	@Reference(check=false)
	private ShareService shareService;
	
	@ApiOperation(value="我的分享列表",notes="我的分享列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页数（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "每页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "type", value = "空为全部，0私密链接分享，1好友分享",dataType = "Integer",paramType="query",required=false),
        @ApiImplicitParam(name = "status", value = "空为全部，0正常，1已失效，2已撤销",dataType = "Integer",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findList")
	public PageInfo<ShareBean> findList(PageInfo<ShareBean> pi,Integer type,Integer status,HttpServletRequest request){
		SessionUserBean user=UserInfoUtils.getBean(request);
		return shareService.findMyShare(pi.getPage(), pi.getLimit(), user.getId(),type,status);
	}
	
	@ApiOperation(value="某个分享下的文件集合",notes="某个分享下的文件集合")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "分享id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "pid", value = "文件父节点（最顶层则为0）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findShareList")
	public Result findShareList(String id,String pid){
		try{
			return ResultUtils.success("查询成功",shareService.findShareFileListFromSelf(id, pid));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="某个分享下的好友集合",notes="某个分享下的好友集合")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "shareid", value = "分享id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findFriends")
	public Result findFriends(String shareid){
		try{
			return ResultUtils.success("查询成功",shareService.findFriends(shareid));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
			
	}
	
	@ApiOperation(value="取消分享",notes="取消分享")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "idjson", value = "勾选文件id（[{'id':'xx'}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/cancelShare")
	public Result cancelShare(String idjson){
		try{
			if(StringUtils.isEmpty(idjson)){
				throw new RuntimeException("请选择取消分享的记录");
			}
			JsonUtils jsonUtils=new JsonJackUtils();
			List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
			List<String> ids=new ArrayList<String>();
			
			if(!CollectionUtils.isEmpty(lists)){
				for(Map m:lists){
					ids.add(m.get("id")==null?"":m.get("id").toString());
				}
			}
			shareService.cancelShare(ids);
			return ResultUtils.success("取消分享成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

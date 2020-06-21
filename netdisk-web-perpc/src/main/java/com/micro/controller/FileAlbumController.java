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
import com.micro.disk.bean.AlbumBean;
import com.micro.disk.bean.FileBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.AlbumService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Api(tags="图片相册")
@RestController
@RequestMapping("/disk/filealbum")
public class FileAlbumController {
	@Reference(check=false)
	private AlbumService albumService;
	
	@ApiOperation(value="相册列表",notes="相册列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "每页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "albumname", value = "相册名称",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findPageList")
	public PageInfo<AlbumBean> findPageList(PageInfo<AlbumBean> pi,String albumname,HttpServletRequest request){
		SessionUserBean user=UserInfoUtils.getBean(request);
		return albumService.findPageList(pi.getPage(), pi.getLimit(), user.getId(), albumname);
	}
	
	@ApiOperation(value="新增相册",notes="新增相册")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumname", value = "相册名称",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "albumdesc", value = "相册描述",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/addAlbum")
	public Result addAlbum(String albumname,String albumdesc,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			albumService.addAlbum(albumname, albumdesc, user.getId(), user.getNickname());
			return ResultUtils.success("新增相册成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="相册单条记录",notes="相册单条记录")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumid", value = "相册id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findOne")
	public Result findOne(String albumid){
		try{
			return ResultUtils.success("查询相册成功", albumService.findOne(albumid));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="删除相册",notes="删除相册、删除相册图片关系")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumid", value = "相册id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/deleteAlbum")
	public Result deleteAlbum(String albumid){
		try{
			albumService.deleteAlbum(albumid);
			return ResultUtils.success("删除相册成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="删除相册",notes="删除相册、删除相册图片关系、删除图片")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumid", value = "相册id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/deleteAlbumCascade")
	public Result deleteAlbumCascade(String albumid,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			albumService.deleteAlbumCascade(user.getId(),user.getNickname(),albumid);
			return ResultUtils.success("删除相册成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="修改相册",notes="修改相册")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumid", value = "相册id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "albumname", value = "相册名称",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "albumdesc", value = "相册描述",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/updateAlbum")
	public Result updateAlbum(String albumid,String albumname,String albumdesc){
		try{
			albumService.updateAlbum(albumid, albumname, albumdesc);
			return ResultUtils.success("修改相册成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="查询相册下的图片列表",notes="查询相册下的图片列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "当前页（从1开始）",dataType = "Integer",paramType="query",required=true),
	    @ApiImplicitParam(name = "limit", value = "每页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "albumid", value = "相册id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findInAlbumImg")
	public PageInfo<FileBean> findInAlbumImg(PageInfo<FileBean> pi,String albumid){
		return albumService.findInAlbumImg(pi.getPage(), pi.getLimit(), albumid);
	}
	
	@ApiOperation(value="查询未分配相册的图片列表",notes="查询未分配相册的图片列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "每页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findNotInAlbumImg")
	public PageInfo<FileBean> findNotInAlbumImg(PageInfo<FileBean> pi,HttpServletRequest request){
		SessionUserBean user=UserInfoUtils.getBean(request);
		return albumService.findNotInAlbumImg(pi.getPage(), pi.getLimit(), user.getId());
	}
	
	@ApiOperation(value="给相册分配图片",notes="给相册分配图片")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumid", value = "相册id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "idjson", value = "图片id的json（[{'id':'xx'}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/setImgToAlbum")
	public Result setImgToAlbum(String albumid,String idjson){
		try{
			if(StringUtils.isEmpty(idjson)){
				throw new RuntimeException("请选择图片");
			}
			JsonUtils jsonUtils=new JsonJackUtils();
			List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
			List<String> ids=new ArrayList<String>();
			
			if(!CollectionUtils.isEmpty(lists)){
				for(Map m:lists){
					ids.add(m.get("id")==null?"":m.get("id").toString());
				}
			}
			
			albumService.setImgToAlbum(albumid, ids);;
			return ResultUtils.success("分配成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="移除图片出某个相册",notes="移除图片出某个相册")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumid", value = "相册ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "idjson", value = "图片id的json（[{'id':'xx'}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/removeImgOutAlbum")
	public Result removeImgOutAlbum(String albumid,String idjson){
		try{
			if(StringUtils.isEmpty(idjson)){
				throw new RuntimeException("请选择图片");
			}
			JsonUtils jsonUtils=new JsonJackUtils();
			List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
			List<String> ids=new ArrayList<>();
			
			for(Map map:lists){
				ids.add(map.get("id").toString());
			}
			
			albumService.removeImgOutAlbum(albumid,ids);
			return ResultUtils.success("成功把图片移出相册成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="移动图片至某个相册",notes="移动图片至某个相册")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumid", value = "相册id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "idjson", value = "图片id的json（[{'id':'xx'}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/moveImgToAlbum")
	public Result moveImgToAlbum(String albumid,String idjson){
		try{
			if(StringUtils.isEmpty(idjson)){
				throw new RuntimeException("请选择图片");
			}
			JsonUtils jsonUtils=new JsonJackUtils();
			List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
			List<String> ids=new ArrayList<>();
			
			for(Map map:lists){
				ids.add(map.get("id").toString());
			}
			albumService.moveImgToAlbum(albumid, ids);
			return ResultUtils.success("移动图片成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="设置相册封面",notes="设置相册封面")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumid", value = "相册ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "fileid", value = "图片ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/setAlbumCover")
	public Result setAlbumCover(String albumid,String fileid){
		try{
			albumService.setAlbumCover(albumid, fileid);
			return ResultUtils.success("设置成相册封面成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="设置相册默认封面",notes="设置相册默认封面")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "albumid", value = "相册ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/deleteAlbumCover")
	public Result deleteAlbumCover(String albumid){
		try{
			albumService.deleteAlbumCover(albumid);
			return ResultUtils.success("设置默认相册封面成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

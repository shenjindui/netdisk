package com.micro.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.FileBean;
import com.micro.disk.bean.FileListBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.SearchBean;
import com.micro.disk.service.FileService;
import com.micro.disk.service.SearchService;
import com.micro.disk.service.TypeSuffixService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Api(tags="文件搜索及列表")
@RestController
@RequestMapping("/disk/file")
public class FileController {
	@Reference(check=false)
	private FileService fileService;
	@Reference(check=false)
	private TypeSuffixService typeSuffixService;
	@Reference(check=false)
	private SearchService searchService;
	
	@ApiOperation(value="Solr库搜索文件",notes="Solr库搜索文件")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页数（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "分页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "filename", value = "文件名称",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/search")
	public PageInfo<SearchBean> search(PageInfo<SearchBean> pi,String filename,HttpServletRequest request){
		SessionUserBean user=UserInfoUtils.getBean(request);
		return searchService.search(filename, user.getId(), pi.getPage(), pi.getLimit());
	}
	
	@ApiOperation(value="全部-列表",notes="全部-列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页数（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "分页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "pid", value = "父节点ID（默认是0）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "orderfield", value = "排序字段",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "ordertype", value = "排序类型",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findList")
	public PageInfo<FileListBean> findList(PageInfo<FileListBean> pi,String pid,String orderfield,String ordertype,HttpServletRequest request){
		SessionUserBean user=UserInfoUtils.getBean(request);
		
		return fileService.findPageList(pi.getPage(), pi.getLimit(), user.getId(), pid,"all", orderfield,ordertype);
	}
	
	@ApiOperation(value="全部-卡片",notes="全部-卡片")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页数（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "分页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "pid", value = "父节点ID（默认是0）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "orderfield", value = "排序字段",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "ordertype", value = "排序类型",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findListCard")
	public PageInfo<FileListBean> findListCard(PageInfo<FileListBean> pi,String pid,String orderfield,String ordertype,HttpServletRequest request){
		SessionUserBean user=UserInfoUtils.getBean(request);
		
		return fileService.findPageListCard(pi.getPage(), pi.getLimit(), user.getId(), pid,"all",orderfield,ordertype);
	}
	
	@ApiOperation(value="具体分类-列表",notes="文档、音频、视频、其他")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页数（从1开始）",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "limit", value = "分页记录数",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "typecode", value = "类型编码",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "filesuffix", value = "文件格式",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "filename", value = "文件名称",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "showtype", value = "展示方式（list,card）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "orderfield", value = "排序字段",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "ordertype", value = "排序类型",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findSpecialList")
	public PageInfo<FileListBean> findSpecialList(PageInfo<FileListBean> pi,String typecode,String filesuffix,String filename,String showtype,
			String orderfield,String ordertype,HttpServletRequest request){
		
		SessionUserBean user=UserInfoUtils.getBean(request);
		
		return fileService.findSpecialList(pi.getPage(), pi.getLimit(), user.getId(), typecode,filesuffix,filename, showtype,orderfield,ordertype);
	}
	
	@ApiOperation(value="查询某种类型对应格式列表",notes="查询某种类型对应格式列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "typecode", value = "类型编码",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findTypeList")
	public Result findTypeList(String typecode){
		try{
			return ResultUtils.success("查询成功", typeSuffixService.findList(typecode));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

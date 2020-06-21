package com.micro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.service.TypeService;
import com.micro.disk.service.TypeSuffixService;

@RestController
@RequestMapping("/type")
public class TypeController {
	@Reference(check=false)
	private TypeService typeService;
	@Reference(check=false)
	private TypeSuffixService typeSuffixService;
	
	/**
	 * 初始化
	 * @return
	 */
	@RequestMapping("/init")
	public Result init(){
		try{
			typeService.init();
			return ResultUtils.success("初始化数据成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 文件类型树形结构
	 * @return
	 */
	@RequestMapping("/findTypeTree")
	public Result findTypeTree(){
		try{
			return ResultUtils.success("查询成功", typeService.findTrees());
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 文件类型下拉框
	 * @return
	 */
	@RequestMapping("/findTypeList")
	public Result findTypeList(){
		try{
			
			return ResultUtils.success("查询成功", typeService.findList());
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

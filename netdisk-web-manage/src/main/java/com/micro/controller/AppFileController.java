package com.micro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.disk.bean.AppFileBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.AppFileService;

@RestController
@RequestMapping("/appfile")
public class AppFileController {
	@Reference(check=false)
	private AppFileService appFileService;
	
	@RequestMapping("/findList")
	public PageInfo<AppFileBean> findList(PageInfo<AppFileBean> pi,String appid,String filename,String filemd5){
		
		return appFileService.findFiles(pi.getPage(), pi.getLimit(),appid,"","", filename, filemd5,true);
	}
}

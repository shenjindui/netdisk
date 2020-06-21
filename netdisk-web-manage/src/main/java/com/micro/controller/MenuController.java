package com.micro.controller;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.xml.MenuBean;
import com.micro.xml.XstreamUtils;

@RestController
@RequestMapping("/menu")
public class MenuController {
	
	@RequestMapping("/findList")
	public Result findMenus(String roleid){	
		try{
			List<MenuBean> data=XstreamUtils.parseMenuxml();
			return ResultUtils.success("查询成功", data);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

package com.micro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.model.LogInfo;
import com.micro.service.LogService;

/**
 * 日志采集
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/log/collect")
public class CollectController {
	@Autowired
	private LogService logService;

	@RequestMapping("/collectLog")
	public void collectLog(LogInfo bean){
		logService.save(bean);
	}
}

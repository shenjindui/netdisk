package com.micro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.dao.LogProjectDao;
import com.micro.model.LogProject;

@Component
public class LogProjectServiceImpl implements LogProjectService{
	@Autowired
	private LogProjectDao logProjectDao;
	
	@Override
	public List<LogProject> findList() {
		return logProjectDao.findAll();
	}

}

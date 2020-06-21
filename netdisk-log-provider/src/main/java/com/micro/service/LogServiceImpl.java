package com.micro.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.micro.dao.LogInfoDao;
import com.micro.jdbc.LogInfoJdbc;
import com.micro.model.LogInfo;
import com.micro.model.PageInfo;

@Component
@Transactional
public class LogServiceImpl implements LogService{
	@Autowired
	private LogInfoJdbc logInfoJdbc;
	@Autowired
	private LogInfoDao logInfoDao;
	
	@Override
	public PageInfo<LogInfo> findList(Integer page, Integer limit, String projectname, String targetmethod,
			String username, String starttime, String endtime,String orderfield,String ordertype) {
		
		return logInfoJdbc.findList(page, limit, projectname, targetmethod, username, starttime, endtime, orderfield, ordertype);
	}

	@Override
	public List<LogInfo> findLogDetail(String traceid) {
		return logInfoDao.findListByTraceid(traceid);
	}

	@Override
	public void save(LogInfo bean) {
		logInfoDao.save(bean);		
	}

}

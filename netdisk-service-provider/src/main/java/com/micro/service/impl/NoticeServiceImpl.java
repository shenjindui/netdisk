package com.micro.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Service;
import com.micro.db.dao.DiskNoticeDao;
import com.micro.db.jdbc.DiskNoticeJdbc;
import com.micro.disk.bean.NoticeBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.NoticeService;
import com.micro.websocket.PushService;

@Service(interfaceClass=NoticeService.class)
@Component
@Transactional
public class NoticeServiceImpl implements NoticeService{
	@Autowired
	private DiskNoticeDao diskNoticeDao;
	@Autowired
	private DiskNoticeJdbc diskNoticeJdbc;
	@Autowired
	private PushService pushService;
	
	@Override
	public PageInfo<NoticeBean> findList(Integer page, Integer limit, String userid) {
		return diskNoticeJdbc.findList(page, limit, userid);
	}

	@Override
	public void updateReadStatus(String userid) {
		diskNoticeDao.updateReadStatus(userid, new Date());
		//推送
		pushService.pushEmptyNotice(userid);
	}

	@Override
	public void delete(String userid) {
		diskNoticeDao.deleteAll(userid);
		//推送
		pushService.pushEmptyNotice(userid);
	}

	@Override
	public List<NoticeBean> findNotices(String userid) {
		
		return diskNoticeJdbc.findNotices(userid);
	}

	@Override
	public int findNoticesCount(String userid) {
		return diskNoticeDao.findNoticesCount(userid);
	}

}

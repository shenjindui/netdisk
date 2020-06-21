package com.micro.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.micro.common.ValidateUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.db.dao.DiskAppDao;
import com.micro.db.jdbc.DiskAppJdbc;
import com.micro.disk.bean.AppBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.UserBean;
import com.micro.disk.service.AppService;
import com.micro.model.DiskApp;

@Service(interfaceClass=AppService.class)
@Component
@Transactional
public class AppServiceImpl implements AppService{
	@Autowired
	private DiskAppDao diskAppDao;
	@Autowired
	private DiskAppJdbc diskAppJdbc;
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	private JsonUtils jsonUtils=new JsonJackUtils();
	
	@Override
	public void save(AppBean bean) {
		ValidateUtils.validate(bean.getAppname(), "应用名称");
		ValidateUtils.validate(bean.getCreateuserid(), "创建人ID");
		ValidateUtils.validate(bean.getCreateusername(), "创建人姓名");
		
		DiskApp app=new DiskApp();
		app.setAppname(bean.getAppname());
		app.setAppdesc(bean.getAppdesc());
		app.setCreateuserid(bean.getCreateuserid());
		app.setCreateusername(bean.getCreateusername());
		app.setCreatetime(new Date());
		app.setDelstatus(0);
		diskAppDao.save(app);
	}

	@Override
	public void update(AppBean bean) {
		ValidateUtils.validate(bean.getId(), "应用ID");
		ValidateUtils.validate(bean.getAppname(), "应用名称");
		
		DiskApp app=diskAppDao.findOne(bean.getId());
		if(app==null){
			throw new RuntimeException("应用ID不存在");
		}
		app.setAppname(bean.getAppname());
		app.setAppdesc(bean.getAppdesc());
		diskAppDao.save(app);
	}

	@Override
	public void delete(String id) {
		DiskApp app=diskAppDao.findOne(id);
		if(app==null){
			throw new RuntimeException("应用ID不存在");
		}
		//删除应用
		app.setDelstatus(1);
		diskAppDao.save(app);
	}

	@Override
	public AppBean findOne(String id) {
		DiskApp app=diskAppDao.findOne(id);
		if(app==null){
			throw new RuntimeException("应用ID不存在");
		}
		AppBean bean=new AppBean();
		BeanUtils.copyProperties(app, bean);
		return bean;
	}

	@Override
	public PageInfo<AppBean> findPageList(Integer page, Integer limit, String name) {
		return diskAppJdbc.findPageList(page, limit, name);
	}

	@Override
	public PageInfo<UserBean> findUserListByAppid(Integer page, Integer limit, String appid, String username) {
		return null;
	}

	@Override
	public boolean checkAppID(String appid) {
		//加缓存
		String json=redisTemplate.opsForValue().get(appid);
		if(StringUtils.isEmpty(json)){			
			DiskApp da=diskAppDao.findOne(appid);
			if(da==null){
				return false;
			}else{
				redisTemplate.opsForValue().set(appid, jsonUtils.objectToJson(da));
				return true;
			}
		}else{
			return true;
		}
	}
}

package com.micro.service.impl;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.common.CapacityUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.db.dao.DiskUserCapacityDao;
import com.micro.db.dao.DiskUserCapacityHistoryDao;
import com.micro.db.jdbc.DiskUserCapacityHistoryJdbc;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.disk.bean.UserCapacityHistoryBean;
import com.micro.disk.service.UserCapacityService;
import com.micro.lock.Lock;
import com.micro.lock.LockContext;
import com.micro.lock.LockZookeeper;
import com.micro.model.DiskUserCapacity;
import com.micro.model.DiskUserCapacityHistory;
/**
 * 常见细节考虑
 * 1、手工删除redis，不删除mysql==》正常
 * 2、手工删除redis和mysql==》上传不了，会报空间不足
 * 3、手工删除mysql，不删除redis==》上传的了，但是mq更新容量的时候报错【暂时不考吧，因为真实环境很少出现这种情况】
 * 		解决1：代码判断
 * 		解决2：canal监听mysql的binlog日志文件
 */
@Service(interfaceClass=UserCapacityService.class)
@Component
@Transactional
public class UserCapacityServiceImpl implements UserCapacityService{
	@Autowired
	private DiskUserCapacityDao diskUserCapacityDao;
	@Autowired
	private DiskUserCapacityHistoryDao diskUserCapacityHistoryDao;
	@Autowired
	private DiskUserCapacityHistoryJdbc diskUserCapacityHistoryJdbc;
	
	@NacosValue(value="${locktype}",autoRefreshed=true)
    private String locktype;
	
	@NacosValue(value="${lockhost}",autoRefreshed=true)
	private String lockhost;
	
	@Override
	public UserCapacityBean findUserCapacity(String userid) {
		DiskUserCapacity capacity=diskUserCapacityDao.findByUserid(userid);
		//方法一：自动初始化100g？？？
		//方法二：提示容量不足
		UserCapacityBean ucb=new UserCapacityBean();
		if(capacity==null){			
			ucb.setUserid(userid);
			ucb.setTotalcapacity(0l);
			ucb.setUsedcapacity(0l);
			ucb.setUsedcapacityname("0B");
			ucb.setTotalcapacityname("0B");
		}else{
			ucb.setUserid(userid);
			ucb.setTotalcapacity(capacity.getTotalcapacity());
			ucb.setUsedcapacity(capacity.getUsedcapacity());
			ucb.setUsedcapacityname(CapacityUtils.convert(ucb.getUsedcapacity()));
			ucb.setTotalcapacityname(CapacityUtils.convert(ucb.getTotalcapacity()));
		}
		return ucb;
	}
	
	@Override
	public PageInfo<UserCapacityHistoryBean> findHistory(Integer page,Integer limit,String userid) {
		return diskUserCapacityHistoryJdbc.findListByUserid(page, limit, userid);
	}
	
	@Override
	public void addUserCapacity(String userid, Long totalcapacity,String capacityunit,String createuserid,String createusername) {
		if(totalcapacity<=0){
			throw new RuntimeException("容量大小不能小于等于0");
		}
		long addtotal=0;
		if("mb".equals(capacityunit)){
			addtotal=totalcapacity*1024*1024;//转换B
		}else if("gb".equals(capacityunit)){
			addtotal=totalcapacity*1024*1024*1024;
		}else if("tb".equals(capacityunit)){
			addtotal=totalcapacity*1024*1024*1024*1024;
		}else{
			throw new RuntimeException("容量单位不正确");
		}
		
		DiskUserCapacity bean=diskUserCapacityDao.findByUserid(userid);
		if(bean==null){
			bean=new DiskUserCapacity();
			bean.setUserid(userid);
			bean.setTotalcapacity(addtotal);
			bean.setUsedcapacity(0l);
			
			diskUserCapacityDao.save(bean);
		}else{
			diskUserCapacityDao.addCapacity(addtotal, userid);
			
		}
		//计算
		DiskUserCapacity duc=diskUserCapacityDao.findByUserid(userid);
		long lefttotal=duc.getTotalcapacity()-duc.getUsedcapacity();
		
		//历史
		DiskUserCapacityHistory history=new DiskUserCapacityHistory();
		history.setUserid(userid);
		history.setRemark("分配容量");
		
		history.setCapacity(addtotal);//新增容量
		history.setType(1);//0减少，1新增
		history.setLeftcapacity(lefttotal);//剩余容量
		
		history.setCreateuserid(createuserid);
		history.setCreateusername(createusername);
		history.setCreatetime(new Date());
		diskUserCapacityHistoryDao.save(history);
	}

	@Override
	public void init(String userid,String username) {
		DiskUserCapacity bean=diskUserCapacityDao.findByUserid(userid);
		if(bean==null){//不存在则初始化
			bean=new DiskUserCapacity();
			
			//容量保存
			bean.setUserid(userid);
			bean.setTotalcapacity(100*1024*1024*1024l);//100G
			bean.setUsedcapacity(0l);
			diskUserCapacityDao.save(bean);
			
			//历史记录保存
			DiskUserCapacityHistory history=new DiskUserCapacityHistory();
			history.setUserid(userid);
			history.setRemark("分配容量");
			
			history.setCapacity(bean.getTotalcapacity());//新增容量
			history.setType(1);//0减少，1新增
			history.setLeftcapacity(bean.getTotalcapacity());//剩余容量
			
			history.setCreateuserid(userid);
			history.setCreateusername(username);
			history.setCreatetime(new Date());
			diskUserCapacityHistoryDao.save(history);
		}
	}
	
	@Override
	public void deleteByUserid(String userid) {
		diskUserCapacityDao.deleteByUserid(userid);
	}

	@Override
	public void updateUserCapacity(int type, long capacity, String userid, String username, String remark) {
		LockContext lockContext=new LockContext(locktype,lockhost);
		try{
			lockContext.getLock("capacity-"+userid);
			
			//更新容量
			if(type==0){//新增已用容量，减少总容量
				diskUserCapacityDao.addUsedCapacity(capacity, userid);		
			}else if(type==1){//减少已用容量，新增总容量
				diskUserCapacityDao.reduceUsedCapacity(capacity, userid);
			}else{
				throw new RuntimeException("type格式不对");
			}
			
			//计算剩余容量
			DiskUserCapacity bean=diskUserCapacityDao.findByUserid(userid);
			if(bean==null){
				throw new RuntimeException("查询不到您的容量记录，无法操作！ ");
			}
			long lefttotal=bean.getTotalcapacity()-bean.getUsedcapacity();
			
			//历史记录
			DiskUserCapacityHistory history=new DiskUserCapacityHistory();
			history.setUserid(userid);
			history.setRemark(remark);
			
			history.setCapacity(capacity);//新增减少的容量
			history.setType(type);//0减少，1新增
			history.setLeftcapacity(lefttotal);//总剩余容量
			
			history.setCreateuserid(userid);
			history.setCreateusername(username);
			history.setCreatetime(new Date());
			diskUserCapacityHistoryDao.save(history);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally{
			lockContext.unLock("capacity-"+userid);
		}
	}
}

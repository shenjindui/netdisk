package com.micro.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.micro.common.DateUtils;
import com.micro.common.ValidateUtils;
import com.micro.db.dao.DiskTypeComponentDao;
import com.micro.db.dao.DiskTypeSuffixOperateDao;
import com.micro.disk.bean.TypeComponentBean;
import com.micro.disk.service.TypeComponentService;
import com.micro.model.DiskTypeComponent;

@Service(interfaceClass=TypeComponentService.class)
@Component
@Transactional
public class TypeComponentServiceImpl implements TypeComponentService{
	@Autowired
	private DiskTypeComponentDao diskTypeComponentDao;
	@Autowired
	private DiskTypeSuffixOperateDao diskTypeSuffixOperateDao;
	
	@Override
	public List<TypeComponentBean> findList() {
		List<DiskTypeComponent> lists=diskTypeComponentDao.findAll();
		List<TypeComponentBean> beans=new ArrayList<TypeComponentBean>();
		
		if(!CollectionUtils.isEmpty(lists)){
			for(DiskTypeComponent dtc:lists){
				TypeComponentBean bean=new TypeComponentBean();
				bean.setId(dtc.getId());
				bean.setCode(dtc.getCode());
				bean.setName(dtc.getName());
				bean.setRemark(dtc.getRemark());
				bean.setCreateuserid(dtc.getCreateuserid());
				bean.setCreateusername(dtc.getCreateusername());
				bean.setCreatetime(DateUtils.formatDate(dtc.getCreatetime(),"yyyy-MM-dd HH:mm:ss"));
				beans.add(bean);
			}
		}
		return beans;
	}

	@Override
	public TypeComponentBean findOne(String id) {
		DiskTypeComponent dtc=diskTypeComponentDao.findOne(id);
		if(dtc==null){
			throw new RuntimeException("主键不存在");
		}
		TypeComponentBean bean=new TypeComponentBean();
		bean.setId(dtc.getId());
		bean.setCode(dtc.getCode());
		bean.setName(dtc.getName());
		bean.setRemark(dtc.getRemark());
		bean.setCreateuserid(dtc.getCreateuserid());
		bean.setCreateusername(dtc.getCreateusername());
		bean.setCreatetime(DateUtils.formatDate(dtc.getCreatetime(),"yyyy-MM-dd HH:mm:ss"));
		return bean;
	}

	@Override
	public void save(TypeComponentBean bean) {
		ValidateUtils.validate(bean.getCode(), "组件编码");
		ValidateUtils.validate(bean.getName(), "组件名称");
		ValidateUtils.validate(bean.getCreateuserid(), "创建人ID");
		ValidateUtils.validate(bean.getCreateusername(), "创建人姓名");
		
		Integer count=diskTypeComponentDao.findCodeIsExistAdd(bean.getCode());
		if(count>0){
			throw new RuntimeException("该编码已经存在,请更换一个!");
		}
		
		DiskTypeComponent dtc=new DiskTypeComponent();
		dtc.setCode(bean.getCode());
		dtc.setName(bean.getName());
		dtc.setRemark(bean.getRemark());
		dtc.setCreateuserid(bean.getCreateuserid());
		dtc.setCreateusername(bean.getCreateusername());
		dtc.setCreatetime(new Date());
		diskTypeComponentDao.save(dtc);
	}

	@Override
	public void update(TypeComponentBean bean) {
		ValidateUtils.validate(bean.getId(), "主键");
		ValidateUtils.validate(bean.getName(), "组件名称");
		DiskTypeComponent dtc=diskTypeComponentDao.findOne(bean.getId());
		if(dtc==null){
			throw new RuntimeException("主键不存在");
		}
		dtc.setName(bean.getName());
		dtc.setRemark(bean.getRemark());
		diskTypeComponentDao.save(dtc);
	}

	@Override
	public void delete(String id) {
		DiskTypeComponent dtc=diskTypeComponentDao.findOne(id);
		if(dtc==null){
			throw new RuntimeException("主键不存在");
		}
		diskTypeComponentDao.delete(id);
		diskTypeSuffixOperateDao.deleteByComponentcode(dtc.getCode());
	}

}

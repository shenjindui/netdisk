package com.micro.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.micro.common.Contanst;
import com.micro.common.DateUtils;
import com.micro.common.ValidateUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.db.dao.DiskTypeComponentDao;
import com.micro.db.dao.DiskTypeDao;
import com.micro.db.dao.DiskTypeSuffixDao;
import com.micro.db.dao.DiskTypeSuffixOperateDao;
import com.micro.db.jdbc.DiskTypeSuffixJdbc;
import com.micro.db.jdbc.DiskTypeSuffixOperateJdbc;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.TypeComponentBean;
import com.micro.disk.bean.TypeSuffixBean;
import com.micro.disk.bean.TypeSuffixOperateBean;
import com.micro.disk.service.TypeSuffixService;
import com.micro.model.DiskTypeComponent;
import com.micro.model.DiskTypeSuffix;
import com.micro.model.DiskTypeSuffixOperate;

@Service(interfaceClass=TypeSuffixService.class)
@Transactional
@Component
public class TypeSuffixServiceImpl implements TypeSuffixService{
	@Autowired
	private DiskTypeSuffixDao diskTypeSuffixDao;
	@Autowired
	private DiskTypeDao diskTypeDao;
	@Autowired
	private DiskTypeSuffixJdbc diskTypeSuffixJdbc;
	@Autowired
	private DiskTypeSuffixOperateDao diskTypeSuffixOperateDao;
	@Autowired
	private DiskTypeSuffixOperateJdbc diskTypeSuffixOperateJdbc;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	private JsonUtils jsonUtils=new JsonJackUtils();
	
	@Override
	public void save(String typecode, String name, String icon,String iconbig, String suffix,List<String> operatecodes) {
		ValidateUtils.validate(typecode, "类型编码");
		ValidateUtils.validate(name, "名称");
		ValidateUtils.validate(icon, "图标(32*32)");
		ValidateUtils.validate(iconbig, "图标(128*128)");
		ValidateUtils.validate(suffix, "格式");
		
		//判断编码是否存在
		Integer count=diskTypeDao.findCount(typecode);
		if(count==0){
			throw new RuntimeException("类型编码不存在");
		}
		
		//判断后缀是否存在
		Integer suffixCount=diskTypeSuffixDao.findCountAdd(suffix);
		if(suffixCount>0){
			throw new RuntimeException("该后缀已经存在");
		}
		//保存
		DiskTypeSuffix bean=new DiskTypeSuffix();
		bean.setTypecode(typecode);
		bean.setName(name);
		bean.setIcon(icon);
		bean.setIconbig(iconbig);
		bean.setSuffix(suffix);
		diskTypeSuffixDao.save(bean);
		
		//保存
		if(!CollectionUtils.isEmpty(operatecodes)){
			for(String code:operatecodes){
				DiskTypeSuffixOperate dtso=new DiskTypeSuffixOperate();
				dtso.setSuffix(suffix);
				dtso.setComponentcode(code);
				diskTypeSuffixOperateDao.save(dtso);
			}
		}
		
		//清空缓存
		stringRedisTemplate.delete(Contanst.PREFIX_FILETYPE+typecode);
	}

	@Override
	public void update(String id, String name, String icon,String iconbig, String suffix,List<String> operatecodes) {
		ValidateUtils.validate(id, "类型主键");
		ValidateUtils.validate(name, "类型名称");
		ValidateUtils.validate(suffix, "类型格式");
		
		DiskTypeSuffix bean=diskTypeSuffixDao.findOne(id);
		if(bean==null){
			throw new RuntimeException("主键不存在");
		}
		
		//判断后缀是否存在
		Integer suffixCount=diskTypeSuffixDao.findCountEdit(id, suffix);
		if(suffixCount>0){
			throw new RuntimeException("该后缀已经存在");
		}
		
		//更新
		bean.setName(name);
		if(!StringUtils.isEmpty(icon)){			
			bean.setIcon(icon);
		}
		if(!StringUtils.isEmpty(iconbig)){			
			bean.setIconbig(iconbig);
		}
		bean.setSuffix(suffix);
		diskTypeSuffixDao.save(bean);
		
		//删除
		diskTypeSuffixOperateDao.deleteBySuffix(suffix);
		//保存
		if(!CollectionUtils.isEmpty(operatecodes)){
			for(String code:operatecodes){
				DiskTypeSuffixOperate dtso=new DiskTypeSuffixOperate();
				dtso.setSuffix(suffix);
				dtso.setComponentcode(code);
				diskTypeSuffixOperateDao.save(dtso);
			}
		}
		
		//清空缓存
		stringRedisTemplate.delete(Contanst.PREFIX_TYPE_SUFFIX+suffix);
		stringRedisTemplate.delete(Contanst.PREFIX_FILETYPE+bean.getTypecode());
	}

	@Override
	public void delete(String id) {
		DiskTypeSuffix bean=diskTypeSuffixDao.findOne(id);
		if(bean==null){
			throw new RuntimeException("主键不存在");
		}
		//删除
		diskTypeSuffixDao.delete(id);
		diskTypeSuffixOperateDao.deleteBySuffix(bean.getSuffix());
		
		//清空缓存
		stringRedisTemplate.delete(Contanst.PREFIX_TYPE_SUFFIX+bean.getSuffix());
		stringRedisTemplate.delete(Contanst.PREFIX_FILETYPE+bean.getTypecode());
	}

	@Override
	public TypeSuffixBean findOne(String id) {
		DiskTypeSuffix bean= diskTypeSuffixDao.findOne(id);
		TypeSuffixBean tsb=new TypeSuffixBean();
		BeanUtils.copyProperties(bean, tsb);
		return tsb;
	}

	@Override
	public PageInfo<TypeSuffixBean> findList(Integer page,Integer limit,String typecode) {
		return diskTypeSuffixJdbc.findList(page, limit, typecode);
	}
	
	@Override
	public List<TypeSuffixBean> findList(String typecode) {
		String obj=stringRedisTemplate.opsForValue().get(Contanst.PREFIX_FILETYPE+typecode);
		List<TypeSuffixBean> rows=new ArrayList<>();
		if(StringUtils.isEmpty(obj)){			
			List<DiskTypeSuffix> lists= diskTypeSuffixDao.findByTypecode(typecode);
			if(!CollectionUtils.isEmpty(lists)){
				for(DiskTypeSuffix m:lists){
					TypeSuffixBean row=new TypeSuffixBean();
					row.setSuffix(m.getSuffix());
					row.setName(m.getName());
					rows.add(row);
				}
				stringRedisTemplate.opsForValue().set(Contanst.PREFIX_FILETYPE+typecode, jsonUtils.objectToJson(rows));
			}
		}else{
			rows=jsonUtils.jsonToList(obj, TypeSuffixBean.class);
		}
		return rows;
	}

	@Override
	public TypeSuffixBean findBySuffix(String suffix) {
		DiskTypeSuffix bean=diskTypeSuffixDao.findBySuffix(suffix);
		TypeSuffixBean tsb=new TypeSuffixBean();
		BeanUtils.copyProperties(bean, tsb);
		return tsb;
	}

	@Override
	public List<TypeSuffixOperateBean> findComponentsBySuffix(String suffix) {
		return diskTypeSuffixOperateJdbc.findListBySuffix(suffix);
	}

	@Override
	public boolean isSupportSuffix(String suffix) {
		DiskTypeSuffix dts=null;
		Object obj=stringRedisTemplate.opsForValue().get(Contanst.PREFIX_TYPE_SUFFIX+suffix);
		if(obj==null){				
			dts=diskTypeSuffixDao.findBySuffix(suffix);
			if(dts==null){
				return false;
			}else{
				stringRedisTemplate.opsForValue().set(Contanst.PREFIX_TYPE_SUFFIX+suffix, jsonUtils.objectToJson(dts));		
				return true;
			}
		}else{
			return true;
		}
	}

}

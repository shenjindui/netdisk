package com.micro.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.micro.db.dao.DiskTypeDao;
import com.micro.db.dao.DiskTypeSuffixDao;
import com.micro.disk.bean.TypeBean;
import com.micro.disk.bean.TypeTree;
import com.micro.disk.service.TypeService;
import com.micro.model.DiskType;
import com.micro.model.DiskTypeSuffix;
import com.micro.xml.TypeSuffixXml;
import com.micro.xml.TypeXml;
import com.micro.xml.XstreamUtils;

@Service(interfaceClass=TypeService.class)
@Transactional
@Component
public class TypeServiceImpl implements TypeService{
	@Autowired
	private DiskTypeDao diskTypeDao;
	@Autowired
	private DiskTypeSuffixDao diskTypeSuffixDao;
	
	@Override
	public void init() {
		//类型
		List<TypeXml> types=XstreamUtils.parseTypexml();
		types.forEach(type->{
			DiskType dt=new DiskType();
			dt.setCode(type.getCode());
			dt.setName(type.getName());
			
			Integer count=diskTypeDao.findCount(type.getCode());
			if(count==0){				
				diskTypeDao.save(dt);
			}
		});
		//类型明细
		List<TypeSuffixXml> details=XstreamUtils.parseTypeDetailxml();
		details.forEach(detail->{
			DiskTypeSuffix dts=new DiskTypeSuffix();
			dts.setTypecode(detail.getTypecode());
			dts.setName(detail.getName());
			dts.setSuffix(detail.getSuffix());
			dts.setIcon(detail.getIcon());
			
			Integer count=diskTypeSuffixDao.findCountAdd(detail.getSuffix());
			if(count==0){				
				diskTypeSuffixDao.save(dts);
			}
		});		
	}

	@Override
	public List<TypeTree> findTrees() {
		List<DiskType> lists= diskTypeDao.findAll();
		List<TypeTree> trees=new ArrayList<TypeTree>();
		List<TypeTree> children=new ArrayList<TypeTree>();
		
		Integer totalcount=0;
		if(lists!=null&&lists.size()!=0){
			for(DiskType bean:lists){
				Integer count=diskTypeSuffixDao.findCountByTypecode(bean.getCode());
				totalcount=totalcount+count;
				
				TypeTree tree=new TypeTree();
				tree.setCode(bean.getCode());
				tree.setLabel(bean.getName()+"("+count+")");
				tree.setChildren(null);
				
				children.add(tree);
			}
		}
		
		TypeTree tree=new TypeTree();
		tree.setCode("all");
		tree.setLabel("全部文件("+totalcount+")");
		tree.setChildren(children);
		
		trees.add(tree);
		return trees;
	}

	@Override
	public List<TypeBean> findList() {
		List<DiskType> types=diskTypeDao.findAll();
		List<TypeBean> beans=new ArrayList<TypeBean>();
		
		if(!CollectionUtils.isEmpty(types)){
			for(DiskType type:types){
				TypeBean bean=new TypeBean();
				BeanUtils.copyProperties(type, bean);
				beans.add(bean);
			}
		}
		return beans;
	}

}

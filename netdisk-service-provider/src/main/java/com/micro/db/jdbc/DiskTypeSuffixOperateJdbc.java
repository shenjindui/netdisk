package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.TypeSuffixOperateBean;

@Component
public class DiskTypeSuffixOperateJdbc {

	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public List<TypeSuffixOperateBean> findListBySuffix(String suffix){
		String sql="select dtc.code,dtc.name "
				+ "from disk_type_component dtc,disk_type_suffix_operate dtso "
				+ "where dtc.code=dtso.componentcode and dtso.suffix=? ";
		Object[] args={
			suffix
		};
		List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
		List<TypeSuffixOperateBean> beans=new ArrayList<TypeSuffixOperateBean>();
		if(!CollectionUtils.isEmpty(lists)){
			for(Map<String,Object> map:lists){
				TypeSuffixOperateBean bean=new TypeSuffixOperateBean();
				bean.setComponentcode(map.get("code").toString());
				bean.setComponentname(map.get("name").toString());
				beans.add(bean);
			}
		}
		return beans;
	}
}

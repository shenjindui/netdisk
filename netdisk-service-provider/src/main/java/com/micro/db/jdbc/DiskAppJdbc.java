package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.AppBean;
import com.micro.disk.bean.PageInfo;

@Component
public class DiskAppJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public PageInfo<AppBean> findPageList(Integer page, Integer limit, String name){
		PageInfo<AppBean> pageInfo=new PageInfo<>();
		try{
			String sql="select dar.*,date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtimes from disk_app_registry dar where delstatus=0 ";
			if(!StringUtils.isEmpty(name)){			
				sql+=" and appname like '%"+name+"%'";
			}
			sql+=" order by createtime desc";
			
			Object[] args={
					
			};
			PageInfo<Map<String,Object>> pi= jdbcTemplate.findPageList(sql, args, page, limit);
			
			List<AppBean> rows=new ArrayList<>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					AppBean ab=new AppBean();
					ab.setId(map.get("id")==null?"":map.get("id").toString());
					ab.setAppname(map.get("appname")==null?"":map.get("appname").toString());
					ab.setAppdesc(map.get("appdesc")==null?"":map.get("appdesc").toString());
					ab.setCreateuserid(map.get("createuserid")==null?"":map.get("createuserid").toString());
					ab.setCreateusername(map.get("createusername")==null?"":map.get("createusername").toString());
					ab.setCreatetime(map.get("createtimes")==null?"":map.get("createtimes").toString());
					rows.add(ab);
				}
			}
			
			
			pageInfo.setPage(page);
			pageInfo.setLimit(limit);
			pageInfo.setRows(rows);
			pageInfo.setTotalElements(pi.getTotalElements());
			pageInfo.setTotalPage(pi.getTotalPage());
			
			pageInfo.setCode(0);
			pageInfo.setMsg("查询成功");
			
			return pageInfo;
		}catch(Exception e){
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
			return pageInfo;
		}
	}
}

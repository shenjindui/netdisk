package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.TypeSuffixBean;

@Component
public class DiskTypeSuffixJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public PageInfo<TypeSuffixBean> findList(Integer page,Integer limit,String typecode){
		PageInfo<TypeSuffixBean> pageInfo=new PageInfo<TypeSuffixBean>();
		try{			
			String sql="select dts.*,"
					+ "(SELECT GROUP_CONCAT(dtc.name) FROM disk_type_component dtc,disk_type_suffix_operate dtso WHERE dtc.code=dtso.componentcode AND dtso.suffix=dts.suffix) AS componentname "
					+ "from disk_type_suffix dts "
					+ "where 1=1";
			if(!"all".equals(typecode)){
				sql=sql+" and typecode='"+typecode+"'";
			}
			Object[] args={
					
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			
			List<TypeSuffixBean> rows=new ArrayList<TypeSuffixBean>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					TypeSuffixBean row=new TypeSuffixBean();
					row.setId(map.get("id")==null?"":map.get("id").toString());
					row.setName(map.get("name")==null?"":map.get("name").toString());
					row.setSuffix(map.get("suffix")==null?"":map.get("suffix").toString());
					row.setIcon(map.get("icon")==null?"":map.get("icon").toString());
					row.setIconbig(map.get("iconbig")==null?"":map.get("iconbig").toString());
					row.setTypecode(map.get("typecode")==null?"":map.get("typecode").toString());
					row.setComponentname(map.get("componentname")==null?"":map.get("componentname").toString());
					rows.add(row);
				}
			}
			pageInfo.setPage(page);
			pageInfo.setLimit(limit);
			pageInfo.setTotalElements(pi.getTotalElements());
			pageInfo.setTotalPage(pi.getTotalPage());
			pageInfo.setRows(rows);
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

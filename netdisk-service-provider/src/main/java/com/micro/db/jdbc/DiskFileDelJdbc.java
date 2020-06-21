package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.common.CapacityUtils;
import com.micro.common.DateUtils;
import com.micro.common.IconConstant;
import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.RubbishBean;

@Component
public class DiskFileDelJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public PageInfo<RubbishBean> findPageList(Integer page, Integer limit, String userid){
		PageInfo<RubbishBean> pageInfo=new PageInfo<>();
		try{			
			String sql="select dfd.*,"
					+ " (select icon from disk_type_suffix where suffix=dfd.filesuffix and typecode=dfd.typecode) as fileicon "
					+ " from disk_file_del dfd "
					+ " where pid=0 and createuserid=? order by deletetime desc";
			Object[] args={
				userid
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<RubbishBean> rows=new ArrayList<>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					RubbishBean row=new RubbishBean();
					row.setId(map.get("id")==null?"":map.get("id").toString());
					
					String filetype=map.get("filetype")==null?"0":map.get("filetype").toString();
					if("0".equals(filetype)){
						row.setFileicon(IconConstant.icon);
					}else{						
						row.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					}
					row.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					row.setFilesize(CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString()));
					row.setDeletetime(map.get("deletetime")==null?"":map.get("deletetime").toString());
					
					//30天过期
					int day=DateUtils.caculateTotalDay(row.getDeletetime(), DateUtils.getCurrentTime());
					int remainday=30-day;
					row.setRemainday(remainday+"天");
					
					rows.add(row);
				}
			}
			
			pageInfo.setPage(page);
			pageInfo.setLimit(limit);
			pageInfo.setRows(rows);
			pageInfo.setTotalElements(pi.getTotalElements());
			pageInfo.setTotalPage(pi.getTotalPage());
			
			pageInfo.setCode(0);
			pageInfo.setMsg("查询成功");
		}catch(Exception e){
			e.printStackTrace();
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
		}
		return pageInfo;
	}
}

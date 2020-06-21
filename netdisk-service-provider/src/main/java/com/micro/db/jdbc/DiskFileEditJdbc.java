package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.FileEditHistoryBean;
import com.micro.disk.bean.PageInfo;

@Component
public class DiskFileEditJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public PageInfo<FileEditHistoryBean> findList(Integer page,Integer limit,String fileid){
		PageInfo<FileEditHistoryBean> pageInfo=new PageInfo<FileEditHistoryBean>();
		try{			
			String sql="select id,fileid,editusername,date_format(edittime,'%Y-%m-%d %H:%i:%S') as edittime,filemd5,prevfilemd5 "
					+ "from disk_file_edit "
					+ "where fileid=? "
					+ "order by edittime desc";
			Object[] args={
				fileid
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<FileEditHistoryBean> rows=new ArrayList<FileEditHistoryBean>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					FileEditHistoryBean row=new FileEditHistoryBean();
					row.setId(map.get("id")==null?"":map.get("id").toString());
					row.setFileid(map.get("fileid")==null?"":map.get("fileid").toString());
					row.setEditusername(map.get("editusername")==null?"":map.get("editusername").toString());
					row.setEdittime(map.get("edittime")==null?"":map.get("edittime").toString());
					row.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
					row.setPrevfilemd5(map.get("prevfilemd5")==null?"":map.get("prevfilemd5").toString());
					rows.add(row);
				}
			}
			pageInfo.setCode(0);
			pageInfo.setMsg("查询成功");
			pageInfo.setPage(page);
			pageInfo.setLimit(limit);
			pageInfo.setTotalElements(pi.getTotalElements());
			pageInfo.setTotalPage(pi.getTotalPage());
			pageInfo.setRows(rows);
		}catch(Exception e){
			e.printStackTrace();
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
		}
		return pageInfo;
	}
}

package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.NoticeBean;
import com.micro.disk.bean.PageInfo;

@Component
public class DiskNoticeJdbc {

	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public List<NoticeBean> findNotices(String userid){
		String sql="select id,type,typename,content,status,"
				+ "date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtime, "
				+ "date_format(readtime,'%Y-%m-%d %H:%i:%S') as readtime "
				+ "from disk_notice "
				+ "where userid=? "
				+ "order by createtime "
				+ "desc limit 0,5";
		Object[] args={
				userid
		};
		List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
		List<NoticeBean> rows=new ArrayList<>();
		if(!CollectionUtils.isEmpty(lists)){
			for(Map<String,Object> map:lists){
				NoticeBean row=new NoticeBean();
				row.setId(map.get("id")==null?"":map.get("id").toString());
				row.setType(map.get("type")==null?"":map.get("type").toString());
				row.setTypename(map.get("typename")==null?"":map.get("typename").toString());
				row.setContent(map.get("content")==null?"":map.get("content").toString());
				row.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
				row.setStatus(map.get("status")==null?"0":map.get("status").toString());
				row.setReadtime(map.get("readtime")==null?"":map.get("readtime").toString());
				rows.add(row);
			}
		}
		return rows;
	}
	public PageInfo<NoticeBean> findList(Integer page, Integer limit, String userid){
		PageInfo<NoticeBean> pageInfo=new PageInfo<>();
		try{			
			String sql="select id,type,typename,content,status,"
					+ "date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtime, "
					+ "date_format(readtime,'%Y-%m-%d %H:%i:%S') as readtime "
					+ "from disk_notice "
					+ "where userid=? order by createtime desc";
			Object[] args={
					userid
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<NoticeBean> rows=new ArrayList<>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					NoticeBean row=new NoticeBean();
					row.setId(map.get("id")==null?"":map.get("id").toString());
					row.setType(map.get("type")==null?"":map.get("type").toString());
					row.setTypename(map.get("typename")==null?"":map.get("typename").toString());
					row.setContent(map.get("content")==null?"":map.get("content").toString());
					row.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					row.setStatus(map.get("status")==null?"0":map.get("status").toString());
					row.setReadtime(map.get("readtime")==null?"":map.get("readtime").toString());
					rows.add(row);
				}
			}
			
			pageInfo.setCode(0);
			pageInfo.setMsg("查询失败");
			pageInfo.setRows(rows);
			pageInfo.setPage(page);
			pageInfo.setLimit(limit);
			pageInfo.setTotalElements(pi.getTotalElements());
			pageInfo.setTotalPage(pi.getTotalPage());
		}catch(Exception e){
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
		}
		return pageInfo;
	}
}

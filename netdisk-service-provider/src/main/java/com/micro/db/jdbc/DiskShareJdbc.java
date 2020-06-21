package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.ShareBean;

@Component
public class DiskShareJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public PageInfo<ShareBean> findMyShare(Integer page, Integer limit, String userid,Integer type,Integer status){
		PageInfo<ShareBean> pageInfo=new PageInfo<>();
		try{
			String sql="select * from disk_share where shareuserid=? ";
			if(type!=null){
				sql+=" and type="+type;
			}
			if(status!=null){
				sql+=" and status="+status;
			}
			sql+=" order by sharetime desc";
			
			Object[] args={
				userid
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<ShareBean> rows=new ArrayList<>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					ShareBean row=new ShareBean();
					row.setId(map.get("id")==null?"":map.get("id").toString());
					row.setTitle(map.get("title")==null?"":map.get("title").toString());
					row.setShareuser(map.get("shareusername")==null?"":map.get("shareusername").toString());
					row.setSharetime(map.get("sharetime")==null?"":map.get("sharetime").toString());
					
					row.setType(Integer.parseInt(map.get("type")==null?"0":map.get("type").toString()));
					row.setUrl(map.get("url")==null?"":map.get("url").toString());
					row.setCode(map.get("code")==null?"":map.get("code").toString());
					
					String effectname=map.get("effect")==null?"0":map.get("effect").toString();
					if("0".equals(effectname)){
						effectname="永久有效";
					}else{
						effectname=effectname+"天";
					}
					row.setEffectname(effectname);
					row.setSharetype(Integer.parseInt(map.get("sharetype")==null?"0":map.get("sharetype").toString()));
					row.setStatus(Integer.parseInt(map.get("status")==null?"0":map.get("status").toString()));
					row.setSavecount(Integer.parseInt(map.get("savecount")==null?"0":map.get("savecount").toString()));
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
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
		}
		return pageInfo;
	}
	public PageInfo<ShareBean> findFriendsShare(Integer page, Integer limit, String userid,Integer status){
		PageInfo<ShareBean> pageInfo=new PageInfo<>();
		try{
			String sql="select ds.id,ds.title,ds.shareusername,ds.sharetime,ds.status, "
					+ " (select count(1) from disk_share_save where shareid=ds.id and userid='"+userid+"') zcount "
					+ " from disk_share ds,disk_share_friends dsf "
					+ " where ds.id=dsf.shareid and dsf.userid=? ";
			if(status!=null){
				sql+=" and ds.status="+status;
			}
			sql+=" order by ds.sharetime desc";
			
			Object[] args={
					userid
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<ShareBean> rows=new ArrayList<>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					ShareBean row=new ShareBean();
					row.setId(map.get("id")==null?"":map.get("id").toString());
					row.setTitle(map.get("title")==null?"":map.get("title").toString());
					row.setShareuser(map.get("shareusername")==null?"":map.get("shareusername").toString());
					row.setSharetime(map.get("sharetime")==null?"":map.get("sharetime").toString());
					row.setStatus(Integer.parseInt(map.get("status")==null?"0":map.get("status").toString()));
					row.setSavecount(Integer.parseInt(map.get("zcount")==null?"0":map.get("zcount").toString()));
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
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
		}
		return pageInfo;
	}
}

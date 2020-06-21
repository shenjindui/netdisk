package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.AppFileBean;
import com.micro.disk.bean.PageInfo;

@Component
public class DiskAppFileJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public PageInfo<AppFileBean> findFiles(Integer page,Integer limit,String appid,
			String userid,String username,String filename,String filemd5,boolean isAdmin){
		PageInfo<AppFileBean> pageInfo=new PageInfo<AppFileBean>();
		try{
			String sql="select t.id,t.appid,t.businessid,t.businesstype,t.filename,t.filesize,t.filesuffix,t.typecode,t.filemd5,t.createuserid,t.createusername,isbreak, "
					+"date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtime, ";
			
					//图标
					if(isAdmin){						
						sql+= " (select icon from disk_type_suffix where suffix=t.filesuffix and typecode=t.typecode) as fileicon, ";
					}
					
					sql+="(select appname from disk_app_registry where id=t.appid) as appname ";
					sql+= "from disk_app_registry_file t ";
					sql+= "where delstatus=0 ";
			if(!StringUtils.isEmpty(appid)){
				sql+=" and appid='"+appid+"'";
			}
			if(!StringUtils.isEmpty(userid)){
				sql+=" and createuserid='"+userid+"'";			
			}
			if(!StringUtils.isEmpty(username)){
				sql+=" and createusername='"+username+"'";			
			}
			if(!StringUtils.isEmpty(filename)){
				sql+=" and filename='"+filename+"'";			
			}
			if(!StringUtils.isEmpty(filemd5)){
				sql+=" and filemd5='"+filemd5+"'";			
			}
			Object[] args={
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<AppFileBean> rows=new ArrayList<AppFileBean>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					AppFileBean bean=new AppFileBean();
					bean.setId(map.get("id")==null?"":map.get("id").toString());
					bean.setAppid(appid);
					bean.setAppname(map.get("appname")==null?"":map.get("appname").toString());
					bean.setBusinessid(map.get("businessid")==null?"":map.get("businessid").toString());
					bean.setBusinesstype(map.get("businesstype")==null?"":map.get("businesstype").toString());
					bean.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					bean.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					bean.setFilesize(map.get("filesize")==null?0l:Long.parseLong(map.get("filesize").toString()));
					bean.setFilesuffix(map.get("filesuffix")==null?"":map.get("filesuffix").toString());
					bean.setTypecode(map.get("typecode")==null?"":map.get("typecode").toString());
					bean.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
					bean.setCreateuserid(map.get("createuserid")==null?"":map.get("createuserid").toString());
					bean.setCreateusername(map.get("createusername")==null?"":map.get("createusername").toString());
					bean.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					bean.setIsbreak(map.get("isbreak")==null?0:Integer.parseInt(map.get("isbreak").toString()));
					rows.add(bean);
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

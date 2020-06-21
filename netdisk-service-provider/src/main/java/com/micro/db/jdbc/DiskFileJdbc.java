package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.micro.common.CapacityUtils;
import com.micro.common.DateUtils;
import com.micro.common.IconConstant;
import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.FileBean;
import com.micro.disk.bean.FileListBean;
import com.micro.disk.bean.PageInfo;
import com.micro.model.DiskFile;
import com.micro.search.bean.FileSearchBean;

@Component
public class DiskFileJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public PageInfo<FileListBean> findAllList(Integer page,Integer limit,String userid,String pid,String typecode,String orderfield,String ordertype){
		PageInfo<FileListBean> pageInfo=new PageInfo<FileListBean>();
		try{
			String sql="select id,filename,filesize,filetype,filemd5,filesuffix,date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtime, "
					+ " (select icon from disk_type_suffix where suffix=df.filesuffix and typecode=df.typecode) as fileicon "
					+ "from disk_file df where createuserid=?";
			//pid
			if(!StringUtils.isEmpty(pid)){
				sql+=" and pid='"+pid+"'";
			}
			
			//typecode
			if(!StringUtils.isEmpty(typecode)&!"all".equals(typecode)&&!"ALL".equals(typecode)){
				sql+=" and typecode='"+typecode+"'";				
			}
			//排序
			if(StringUtils.isEmpty(orderfield)){
				sql+=" order by createtime desc";
			}else{
				ordertype=ordertype.replace("ending", "");
				sql+=" order by "+orderfield+" "+ordertype;	
			}
			
			Object[] args={
				userid
			};
			
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<FileListBean> rows=new ArrayList<FileListBean>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					FileListBean fb=new FileListBean();
					fb.setId(map.get("id")==null?"":map.get("id").toString());
					fb.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					fb.setFilesuffix(map.get("filesuffix")==null?"":map.get("filesuffix").toString());
					fb.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
					fb.setFiletype(Integer.parseInt(map.get("filetype")==null?"0":map.get("filetype").toString()));
					if(fb.getFiletype()==0){
						fb.setFileicon(IconConstant.icon);
					}else{						
						fb.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					}
					
					String size=CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString());					
					fb.setFilesizename(size);
					fb.setFilesize(Long.parseLong(map.get("filesize")==null?"0":map.get("filesize").toString()));
					
					fb.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					rows.add(fb);
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
	public PageInfo<FileListBean> findAllListCard(Integer page,Integer limit,String userid,String pid,String orderfield,String ordertype){
		PageInfo<FileListBean> pageInfo=new PageInfo<FileListBean>();
		try{
			String sql="select id,filename,filesize,filetype,filemd5,filesuffix,typecode,thumbnailurl,date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtime, "
					+ " (select iconbig from disk_type_suffix where suffix=df.filesuffix and typecode=df.typecode) as fileicon "
					/*+ " ("
					+ " CASE typecode "
					+ " WHEN 'picture' THEN (SELECT thumbnailurl FROM disk_md5 WHERE MD5=df.filemd5) "
					+ " ELSE '' END "
					+ " ) as thumbnailurl "*/
					
					+ "from disk_file df where createuserid=?";
			//pid
			if(!StringUtils.isEmpty(pid)){
				sql+=" and pid='"+pid+"'";
			}
			//排序
			if(StringUtils.isEmpty(orderfield)){
				sql+=" order by createtime desc";
			}else{
				ordertype=ordertype.replace("ending", "");
				sql+=" order by "+orderfield+" "+ordertype;	
			}
			
			Object[] args={
				userid
			};
			
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<FileListBean> rows=new ArrayList<FileListBean>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					FileListBean fb=new FileListBean();
					fb.setId(map.get("id")==null?"":map.get("id").toString());
					fb.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					fb.setFilesuffix(map.get("filesuffix")==null?"":map.get("filesuffix").toString());
					fb.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
					fb.setFiletype(Integer.parseInt(map.get("filetype")==null?"0":map.get("filetype").toString()));
					if(fb.getFiletype()==0){
						fb.setFileicon(IconConstant.iconbig);
					}else{						
						fb.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					}
					fb.setThumbnailurl(map.get("thumbnailurl")==null?"":map.get("thumbnailurl").toString());
					fb.setTypecode(map.get("typecode")==null?"":map.get("typecode").toString());
					
					String size=CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString());					
					fb.setFilesizename(size);
					fb.setFilesize(Long.parseLong(map.get("filesize")==null?"0":map.get("filesize").toString()));
					fb.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					rows.add(fb);
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
	
	//文档、视频、音乐、其他
	public PageInfo<FileListBean> findSpecialList(Integer page,Integer limit,String userid,String typecode,String filesuffix,String filename,String showtype,String orderfield,String ordertype){
		PageInfo<FileListBean> pageInfo=new PageInfo<FileListBean>();
		try{
			String sql="select id,filename,filesize,filetype,filemd5,filesuffix,typecode,date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtime,pid,"
					+ "(select filename from disk_file where id=df.pid) as pname ";
					if("list".equals(showtype)){
						sql+=",(select icon from disk_type_suffix where suffix=df.filesuffix and typecode=df.typecode) as fileicon ";
					}else if("card".equals(showtype)){
						sql+=",(select iconbig from disk_type_suffix where suffix=df.filesuffix and typecode=df.typecode) as fileicon ";				
					}
					sql+= "from disk_file df where createuserid=?";
					
			//typecode
			if(!StringUtils.isEmpty(typecode)&!"all".equals(typecode)&&!"ALL".equals(typecode)){
				sql+=" and typecode='"+typecode+"'";				
			}
			//filesuffix
			if(!StringUtils.isEmpty(filesuffix)){
				sql+=" and filesuffix='"+filesuffix+"'";
			}
			//filename
			if(!StringUtils.isEmpty(filename)){
				sql+=" and filename like '%"+filename+"%'";
			}
			//排序
			if(StringUtils.isEmpty(orderfield)){
				sql+=" order by createtime desc";
			}else{
				ordertype=ordertype.replace("ending", "");
				sql+=" order by "+orderfield+" "+ordertype;	
			}
			
			Object[] args={
				userid
			};
			
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<FileListBean> rows=new ArrayList<FileListBean>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					FileListBean fb=new FileListBean();
					fb.setId(map.get("id")==null?"":map.get("id").toString());
					fb.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					fb.setFilesuffix(map.get("filesuffix")==null?"":map.get("filesuffix").toString());
					fb.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
					fb.setFiletype(Integer.parseInt(map.get("filetype")==null?"0":map.get("filetype").toString()));
					fb.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					fb.setTypecode(map.get("typecode")==null?"":map.get("typecode").toString());
					fb.setPid(map.get("pid")==null?"":map.get("pid").toString());
					fb.setPname(map.get("pname")==null?"":map.get("pname").toString());
					
					String size=CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString());					
					fb.setFilesizename(size);
					fb.setFilesize(Long.parseLong(map.get("filesize")==null?"0":map.get("filesize").toString()));
					fb.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					rows.add(fb);
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
	
	public List<DiskFile> findFolderTree(String userid,String pid,List<String> ids){
		String sql="select id,pid,filename,filesize,date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtime "
				+ "from disk_file where createuserid=? and pid=? and filetype=0 ";
		//方式一：not in性能太差劲
		/*sql+=" and id not in ( ";
		
		for(int i=0;i<ids.size();i++){
			if(i==ids.size()-1){				
				sql+=" '"+ids.get(i)+"' ";
			}else{				
				sql+=" '"+ids.get(i)+"', ";
			}
		}
		
		sql+=" )";*/
		//方式二：
		for(String id:ids){
			sql+=" and id!='"+id+"' ";
		}
		
		sql+=" order by createtime desc";
		
		Object[] args={
			userid,
			pid
		};
		List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
		List<DiskFile> files=new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(lists)){
			lists.forEach(m->{
				DiskFile file=new DiskFile();
				file.setId(m.get("id").toString());
				file.setPid(pid);
				file.setFilename(m.get("filename").toString());
				file.setFilesize(0l);
				file.setCreatetime(DateUtils.parseDate(m.get("createtime").toString(), "yyyy-MM-dd HH:mm:ss"));
				
				files.add(file);
			});
		}
		
		return files;
	}
	
	///////////////////////////////////////////【以下两个方法是Solr数据同步专用】//////////////////////////////////////////////////
	
	public List<FileSearchBean> findLists(){
		try{			
			String sql="select id,filename,pid,filemd5,typecode,filesuffix,filesize,filetype,createuserid,createusername,"
					+ "date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtime,"
					+ "(select icon from disk_type_suffix where typecode=df.typecode and suffix=df.filesuffix) as fileicon "
					+ "from disk_file df";
			Object[] args={
				
			};
			List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
			List<FileSearchBean> beans=new ArrayList<FileSearchBean>();
			
			if(!CollectionUtils.isEmpty(lists)){
				for(Map<String,Object> map:lists){					
					FileSearchBean bean=new FileSearchBean();
					bean.setId(map.get("id")==null?"":map.get("id").toString());
					bean.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					bean.setPid(map.get("pid")==null?"0":map.get("pid").toString());
					bean.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
					bean.setTypecode(map.get("typecode")==null?"":map.get("typecode").toString());
					bean.setFilesuffix(map.get("filesuffix")==null?"":map.get("filesuffix").toString());
					bean.setFilesize(map.get("filesize")==null?"":map.get("filesize").toString());
					bean.setFiletype(map.get("filetype")==null?"0":map.get("filetype").toString());
					bean.setCreateuserid(map.get("createuserid")==null?"":map.get("createuserid").toString());
					bean.setCreateusername(map.get("createusername")==null?"":map.get("createusername").toString());
					bean.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					
					if("0".equals(bean.getFiletype())){
						bean.setFileicon(IconConstant.icon);
					}else{					
						bean.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					}
					
					beans.add(bean);
				}
			}
			
			return beans;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	public FileSearchBean findOne2(String id){
		String sql="select id,filename,pid from disk_file df where id=?";
		Object[] args={
			id
		};
		Map<String,Object> map=jdbcTemplate.findOne(sql, args);
		FileSearchBean bean=new FileSearchBean();
		if(!CollectionUtils.isEmpty(map)){
			bean.setId(map.get("id")==null?"":map.get("id").toString());
			bean.setFilename(map.get("filename")==null?"":map.get("filename").toString());
			bean.setPid(map.get("pid")==null?"0":map.get("pid").toString());
		}
		return bean;
	}
}

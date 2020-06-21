package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.micro.common.CapacityUtils;
import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.Md5Bean;
import com.micro.disk.bean.PageInfo;

@Component
public class DiskMd5Jdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;

	public PageInfo<Md5Bean> findList(Integer page,Integer limit,String filename,String md5){
		PageInfo<Md5Bean> pageInfo=new PageInfo<>();
		try{			
			String sql="select id,md5,filenum,typecode,filename,filesuffix,filesize,thumbnailurl,imgsize, "
					+ " (select icon from disk_type_suffix where suffix=dm.filesuffix and typecode=dm.typecode) as fileicon "
					+ "from disk_md5 dm where 1=1";
			
			if(!StringUtils.isEmpty(filename)){
				sql+=" and filename like '%"+filename+"%'";
			}
			if(!StringUtils.isEmpty(md5)){
				sql+=" and md5='"+md5+"'";
			}
			
			Object[] args={
					
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<Md5Bean> rows=new ArrayList<Md5Bean>();
			
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					Md5Bean row=new Md5Bean();
					row.setId(map.get("id")==null?"":map.get("id").toString());
					row.setMd5(map.get("md5")==null?"":map.get("md5").toString());
					row.setFilenum(Long.parseLong(map.get("filenum")==null?"0":map.get("filenum").toString()));
					row.setTypecode(map.get("typecode")==null?"":map.get("typecode").toString());
					row.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					row.setFilesuffix(map.get("filesuffix")==null?"":map.get("filesuffix").toString());
					row.setFilesize(Long.parseLong(map.get("filesize")==null?"0":map.get("filesize").toString()));
					row.setFilesizename(CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString()));
					row.setThumbnailurl(map.get("thumbnailurl")==null?"":map.get("thumbnailurl").toString());
					row.setImgsize(map.get("imgsize")==null?"":map.get("imgsize").toString());
					row.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
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
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
		}
		return pageInfo;
	}
}

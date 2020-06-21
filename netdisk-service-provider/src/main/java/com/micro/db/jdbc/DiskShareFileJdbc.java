package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.common.CapacityUtils;
import com.micro.common.IconConstant;
import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.ShareFileBean;

@Component
public class DiskShareFileJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public List<ShareFileBean> findListChild(String shareid,String pid){
		try{			
			String sql="select *,"
					+ "date_format(dsf.createtime,'%Y-%m-%d %H:%i:%S') as createtimes, "
					+ " (select icon from disk_type_suffix where suffix=dsf.filesuffix and typecode=dsf.typecode) as fileicon "
					+ "from disk_share_file dsf "
					+ "where shareid=? and pid=? order by createtime desc";
			Object[] args={
					shareid,
					pid
			};
			List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
			List<ShareFileBean> files=new ArrayList<>();
			if(!CollectionUtils.isEmpty(lists)){
				for(Map map:lists){
					ShareFileBean bean=new ShareFileBean();
					bean.setId(map.get("id")==null?"":map.get("id").toString());
					bean.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					bean.setFilesize(CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString()));
					bean.setFiletype(Integer.parseInt(map.get("filetype")==null?"0":map.get("filetype").toString()));
					bean.setCreatetime(map.get("createtimes")==null?"":map.get("createtimes").toString());
					if(bean.getFiletype()==0){
						bean.setFileicon(IconConstant.icon);
					}else{						
						bean.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					}
					
					files.add(bean);
				}
			}
			return files;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("查询失败");
		}
	}
}

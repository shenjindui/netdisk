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
import com.micro.disk.bean.AlbumBean;
import com.micro.disk.bean.FileBean;
import com.micro.disk.bean.PageInfo;

@Component
public class DiskAlbumJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public PageInfo<AlbumBean> findList(Integer page, Integer limit, String userid, String albumname){
		PageInfo<AlbumBean> pageInfo=new PageInfo<>();
		try{			
			String sql="select da.*,"
					+ "date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtimes,"
					+ "(select count(1) from disk_album_file where albumid=da.id) as count "
					+ "from disk_album da where createuserid=? ";
			
			if(!StringUtils.isEmpty(albumname)){
				sql+=" and albumname like '%"+albumname+"%' ";
			}
			
			sql+="order by createtime desc";
			Object[] args={
				userid
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			
			List<Map<String,Object>> lists=pi.getRows();
			List<AlbumBean> rows=new ArrayList<AlbumBean>();
			if(!CollectionUtils.isEmpty(lists)){
				lists.forEach(m->{
					AlbumBean row=new AlbumBean();
					row.setId(m.get("id")==null?"":m.get("id").toString());
					row.setAlbumname(m.get("albumname")==null?"":m.get("albumname").toString());
					row.setAlbumdesc(m.get("albumdesc")==null?"":m.get("albumdesc").toString());
					row.setCount(m.get("count")==null?"":m.get("count").toString());
					row.setCreatetime(m.get("createtimes")==null?"":m.get("createtimes").toString());
					row.setCoverurl(m.get("coverurl")==null?"":m.get("coverurl").toString());
					rows.add(row);
				});
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
	
	public List<AlbumBean> findList(String userid){
		String sql="select da.*,"
				+ "formatdate(da.createtime) as createtimes,"
				+ "(select count(1) from disk_album_file where albumid=da.id) as count "
				+ "from disk_album da where createuserid=? order by createtime desc";
		Object[] args={
			userid
		};
		List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
		List<AlbumBean> rows=new ArrayList<AlbumBean>();
		if(!CollectionUtils.isEmpty(lists)){
			lists.forEach(m->{
				AlbumBean row=new AlbumBean();
				row.setId(m.get("id")==null?"":m.get("id").toString());
				row.setAlbumname(m.get("albumname")==null?"":m.get("albumname").toString());
				row.setAlbumdesc(m.get("albumdesc")==null?"":m.get("albumdesc").toString());
				row.setCount(m.get("count")==null?"":m.get("count").toString());
				row.setCreatetime(m.get("createtimes")==null?"":m.get("createtimes").toString());
				row.setCoverurl(m.get("coverurl")==null?"":m.get("coverurl").toString());
				rows.add(row);
			});
		}
		
		return rows;
	}
	
	/**
	 * 查询某个相册下的图片分页列表
	 * @param page
	 * @param limit
	 * @param albumid
	 * @return
	 */
	public PageInfo<FileBean> findInAlbumImg(Integer page,Integer limit,String albumid){
		PageInfo<FileBean> pageInfo=new PageInfo<>();
		try{
			
			page=page==null?1:page;
			limit=limit==null?10:limit;
			
			String sql="select df.id,df.filename,df.filesize,df.filetype,df.thumbnailurl,df.imgsize,df.filemd5,df.createusername,"
					+ " date_format(df.createtime,'%Y-%m-%d %H:%i:%S') as createtime, "
					+ " (select icon from disk_type_suffix where suffix=df.filesuffix and typecode=df.typecode) as fileicon "
					//+ " (SELECT thumbnailurl FROM disk_md5 WHERE MD5=df.filemd5) as thumbnailurl "
					+ "from disk_file df,disk_album_file daf "
					+ "where df.id=daf.fileid and daf.albumid=? ";
			sql+=" order by daf.createtime desc";	
			
			Object[] args={
				albumid
			};
			
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<FileBean> rows=new ArrayList<>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					FileBean fb=new FileBean();
					fb.setId(map.get("id")==null?"":map.get("id").toString());
					fb.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					String size=CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString());					
					fb.setFilesize(size);
					fb.setFiletype(Integer.parseInt(map.get("filetype")==null?"0":map.get("filetype").toString()));
					fb.setCreateusername(map.get("createusername")==null?"":map.get("createusername").toString());
					fb.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					fb.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					fb.setThumbnailurl(map.get("thumbnailurl")==null?"":map.get("thumbnailurl").toString());
					fb.setImgsize(map.get("imgsize")==null?"":map.get("imgsize").toString());
					fb.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
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
	
	/**
	 * 相册分享，查询其下面的图片
	 * @param albumid
	 * @return
	 */
	public List<FileBean> findInAlbumImg(String albumid){
		try{
			String sql="select df.id,df.filename,df.filesize,df.filesuffix,df.typecode,df.filemd5,df.filetype,df.thumbnailurl,df.imgsize,df.createuserid,df.createusername,"
					+ "date_format(df.createtime,'%Y-%m-%d %H:%i:%S') as createtime, "
					+ " (select icon from disk_type_suffix where suffix=df.filesuffix and typecode=df.typecode) as fileicon "
					//+ " (SELECT thumbnailurl FROM disk_md5 WHERE MD5=df.filemd5) as thumbnailurl "
					+ "from disk_file df,disk_album_file daf "
					+ "where df.id=daf.fileid and daf.albumid=? ";
			sql+=" order by daf.createtime desc";	
			
			Object[] args={
				albumid
			};
			
			List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
			List<FileBean> rows=new ArrayList<>();
			if(!CollectionUtils.isEmpty(lists)){
				for(Map<String,Object> map:lists){
					FileBean fb=new FileBean();
					fb.setId(map.get("id")==null?"":map.get("id").toString());
					fb.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					fb.setFilesize(map.get("filesize")==null?"0":map.get("filesize").toString());
					
					fb.setFilesuffix(map.get("filesuffix")==null?"":map.get("filesuffix").toString());
					fb.setTypecode(map.get("typecode")==null?"":map.get("typecode").toString());
					fb.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
					
					fb.setFiletype(Integer.parseInt(map.get("filetype")==null?"0":map.get("filetype").toString()));
					fb.setCreateuserid(map.get("createuserid")==null?"":map.get("createuserid").toString());
					fb.setCreateusername(map.get("createusername")==null?"":map.get("createusername").toString());
					fb.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					
					fb.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					fb.setThumbnailurl(map.get("thumbnailurl")==null?"":map.get("thumbnailurl").toString());
					fb.setImgsize(map.get("imgsize")==null?"":map.get("imgsize").toString());
					rows.add(fb);
				}
			}
			return rows;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public PageInfo<FileBean> findNotInAlbumImg(Integer page,Integer limit,String userid){
		PageInfo<FileBean> pageInfo=new PageInfo<>();
		try{
			
			page=page==null?1:page;
			limit=limit==null?10:limit;
			
			//类型是图片，创建人，没有在disk_album_file存在
			String sql="select df.id,df.filename,df.filesize,df.filetype,df.thumbnailurl,df.imgsize,df.filemd5,df.createusername,"
					+ " date_format(df.createtime,'%Y-%m-%d %H:%i:%S') as createtime,"
					+ " (select icon from disk_type_suffix where suffix=df.filesuffix and typecode=df.typecode) as fileicon "
					//+ " (SELECT thumbnailurl FROM disk_md5 WHERE MD5=df.filemd5) as thumbnailurl "
					+ "from disk_file df "
					+ "where typecode='picture' and df.createuserid=? "
					+ "and (select count(1) from disk_album_file where fileid=df.id)=0 ";
			sql+=" order by df.createtime desc";	
			
			Object[] args={
				userid
			};
			
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<FileBean> rows=new ArrayList<>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					FileBean fb=new FileBean();
					fb.setId(map.get("id")==null?"":map.get("id").toString());
					fb.setFilename(map.get("filename")==null?"":map.get("filename").toString());
					String size=CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString());					
					fb.setFilesize(size);
					fb.setFiletype(Integer.parseInt(map.get("filetype")==null?"0":map.get("filetype").toString()));
					fb.setCreateusername(map.get("createusername")==null?"":map.get("createusername").toString());
					fb.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					fb.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
					fb.setThumbnailurl(map.get("thumbnailurl")==null?"":map.get("thumbnailurl").toString());
					fb.setImgsize(map.get("imgsize")==null?"":map.get("imgsize").toString());
					fb.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
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
}

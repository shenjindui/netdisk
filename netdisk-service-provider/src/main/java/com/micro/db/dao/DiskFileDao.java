package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskFile;

public interface DiskFileDao extends JpaRepository<DiskFile, String>,JpaSpecificationExecutor<DiskFile> {
	
	/**
	 * 查询文件数
	 * @return
	 */
	@Query("select count(1) from DiskFile t")
	public Long findFileNum();
	
	/**
	 * 查询文件大小
	 * @return
	 */
	@Query("select sum(filesize) from DiskFile t")
	public Long findFileSize();
	
	/**
	 * 文件夹-不能有重复【文件夹上传】
	 * @param userid
	 * @param pid
	 * @param filename
	 * @param filetype
	 * @return
	 */
	@Query("select t from DiskFile t where createuserid=?1 and pid=?2 and filename=?3 and filetype=0")
	public DiskFile findFolder(String userid,String pid,String filename);
	
	/**
	 * 文件夹-不能有重复【创建文件夹】
	 * @param userid
	 * @param pid
	 * @param filename
	 * @param filetype
	 * @return
	 */
	@Query("select count(1) from DiskFile t where createuserid=?1 and pid=?2 and filename=?3 and filetype=0")
	public Integer findFolderIsExistAdd(String userid,String pid,String filename);
	
	/**
	 * 文件/文件夹-不能有重复【重命名】
	 * @param userid
	 * @param pid
	 * @param id
	 * @param name
	 * @param type
	 */
	@Query("select count(1) from DiskFile t where createuserid=?1 and pid=?2 and id!=?3 and filename=?4 and filetype=?5")
	public Integer findFilenameIsExistEdit(String userid,String pid,String id,String name,Integer filetype);
	
	/**
	 * 复制、移动、数据还原、分享转存--控制名称不能相同
	 * @param userid
	 * @param pid
	 * @param name
	 * @param filetype
	 * @return
	 */
	@Query("select t from DiskFile t where createuserid=?1 and pid=?2 and filename=?3 and filetype=0")
	public DiskFile findFolderNameIsExist(String userid,String pid,String name);
	/**
	 * 复制、移动、数据还原、分享转存--控制名称不能相同
	 * @param userid
	 * @param pid
	 * @param filemd5
	 * @param name
	 * @return
	 */
	@Query("select t from DiskFile t where createuserid=?1 and pid=?2 and filemd5=?3 and filename=?4 and filetype=1")
	public DiskFile findFileNameIsExist(String userid,String pid,String filemd5,String name);
	
	/**
	 * 文件-不能重复【上传】
	 * @param userid
	 * @param pid
	 * @param filename
	 * @param filemd5
	 * @return
	 */
	@Query("select t from DiskFile t where createuserid=?1 and pid=?2 and filename=?3 and filemd5=?4 and filetype=1")
	public DiskFile findFile(String userid,String pid,String filename,String filemd5);
	
	/**
	 * 根据pid查询集合
	 * @param pid
	 * @return
	 */
	@Query("select t from DiskFile t where createuserid=?1 and pid=?2 order by createtime asc")
	public List<DiskFile> findListByPid(String userid,String pid);
	
	/**
	 * 查询某个用户的文件树（文件夹+文件）
	 * @param userid
	 * @return
	 */
	//@Query("select t.id,t.filename,t.pid from DiskFile t where createuserid=?1 and delstatus=0 order by createtime desc")
	//public List<DiskFile> findFoldAndFileTree(String userid);	
	
	/**
	 * 用于复制到、移动到--文件夹树【懒加载】
	 * @param userid
	 * @return
	 */
	@Query("select t from DiskFile t where createuserid=?1 and pid=?2 and id!=?3 and filetype=0 order by createtime desc")
	public List<DiskFile> findFolderTree(String userid,String pid,String id);	
	
	/**
	 * 用于转存--文件夹树【懒加载】
	 * @param userid
	 * @param pid
	 * @return
	 */
	@Query("select t from DiskFile t where createuserid=?1 and pid=?2 and filetype=0 order by createtime desc")
	public List<DiskFile> findFolderTree(String userid,String pid);	
	
	/**
	 * 重命名
	 * @param id
	 * @param name
	 */
	@Modifying
	@Query("update DiskFile t set filename=?2 where id=?1")
	public void rename(String id,String name);
	
	/**
	 * 更新字段
	 * @param thumbnailurl
	 * @param imgsize
	 * @param md5
	 */
	@Modifying
	@Query("update DiskFile set thumbnailurl=?1,imgsize=?2 where id=?3")
	public void updateField(String thumbnailurl,String imgsize,String id);
	
	@Modifying
	@Query("update DiskFile set filemd5=?1,filesize=?2 where id=?3")
	public void updateField(String filemd5,long filesize,String id);
}

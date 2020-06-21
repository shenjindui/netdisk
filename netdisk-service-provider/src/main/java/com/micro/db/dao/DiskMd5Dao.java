package com.micro.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskMd5;

public interface DiskMd5Dao extends JpaRepository<DiskMd5, String>,JpaSpecificationExecutor<DiskMd5> {

	/**
	 * 判断md5是否存在
	 * @param md5
	 * @return
	 */
	@Query("select t from DiskMd5 t where md5=?1")
	public DiskMd5 findMd5IsExist(String md5);

	/**
	 * 查询文件数
	 * @return
	 */
	@Query("select count(1) from DiskMd5 t")
	public Long findFileNum();
	
	/**
	 * 查询文件大小
	 * @return
	 */
	@Query("select sum(filesize) from DiskMd5 t")
	public Long findFileSize();
	
	/**
	 * 更新字段
	 * @param thumbnailurl
	 * @param imgsize
	 * @param md5
	 */
	@Modifying
	@Query("update DiskMd5 set thumbnailurl=?1,imgsize=?2 where md5=?3")
	public void updateField(String thumbnailurl,String imgsize,String md5);
}

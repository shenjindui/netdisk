package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskMd5Chunk;

public interface DiskMd5ChunkDao extends JpaRepository<DiskMd5Chunk, String>,JpaSpecificationExecutor<DiskMd5Chunk> {
	//根据md5获取所有切块
	@Query("select t from DiskMd5Chunk t where filemd5=?1 order by chunknumber asc")
	public List<DiskMd5Chunk> findList(String filemd5);
	
	//判断切块是否存在
	//@Query("select count(1) from DiskMd5Chunk t where filemd5=?1 and chunknumber=?2")
	//public Integer findChunkIsExist(String filemd5,Integer chunknumber);
	
	/**
	 * 查询切块数
	 * @return
	 */
	@Query("select count(1) from DiskMd5Chunk t")
	public Long findChunkNum();
	
	//查询切块是否存在
	@Query("select count(1) from DiskMd5Chunk t where storepath=?1")
	public int findCountByStorepath(String storepath);
}

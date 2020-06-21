package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskTypeSuffix;

public interface DiskTypeSuffixDao extends JpaRepository<DiskTypeSuffix, String>,JpaSpecificationExecutor<DiskTypeSuffix> {
	/**
	 * 根据后缀判断是否已经存在
	 * @param suffix
	 * @return
	 */
	@Query("select count(1) from DiskTypeSuffix t where suffix=?1")
	public Integer findCountAdd(String suffix);
	
	/**
	 * 根据后缀判断是否已经存在
	 * @param suffix
	 * @return
	 */
	@Query("select count(1) from DiskTypeSuffix t where id!=?1 and suffix=?2")
	public Integer findCountEdit(String id,String suffix);
	
	/**
	 * 根据typecode查询集合
	 * @param typeid
	 * @return
	 */
	@Query("select t from DiskTypeSuffix t where typecode=?1")
	public List<DiskTypeSuffix> findByTypecode(String typecode);
	
	/**
	 * 根据typecode查询数量
	 * @param typecode
	 * @return
	 */
	@Query("select count(1) from DiskTypeSuffix t where typecode=?1")
	public Integer findCountByTypecode(String typecode);
	
	/**
	 * 根据后缀查询
	 * @param suffix
	 * @return
	 */
	@Query("select t from DiskTypeSuffix t where suffix=?1")
	public DiskTypeSuffix findBySuffix(String suffix);
	
	/**
	 * 根据typecode删除
	 * @param typeid
	 */
	@Modifying
	@Query("delete DiskTypeSuffix where typecode=?1")
	public void deleteByTypeid(String typecode);
}

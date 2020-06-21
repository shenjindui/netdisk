package com.micro.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskType;

public interface DiskTypeDao extends JpaRepository<DiskType, String>,JpaSpecificationExecutor<DiskType> {

	@Query("select count(1) from DiskType t where code=?1")
	public Integer findCount(String code);
}

package com.micro.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskTypeComponent;

public interface DiskTypeComponentDao extends JpaRepository<DiskTypeComponent, String>,JpaSpecificationExecutor<DiskTypeComponent>{

	@Query("select count(1) from DiskTypeComponent where code=?1")
	public Integer findCodeIsExistAdd(String code);	
}

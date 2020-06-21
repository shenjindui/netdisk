package com.micro.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskShare;

public interface DiskShareDao extends JpaRepository<DiskShare, String>,JpaSpecificationExecutor<DiskShare> {

	@Modifying
	@Query("update DiskShare set savecount=savecount+1 where id=?")
	public void updateCount(String id);
}

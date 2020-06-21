package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskTypeSuffixOperate;

public interface DiskTypeSuffixOperateDao extends JpaRepository<DiskTypeSuffixOperate, String>,JpaSpecificationExecutor<DiskTypeSuffixOperate>{

	@Modifying
	@Query("delete from DiskTypeSuffixOperate where suffix=?1")
	public void deleteBySuffix(String suffix);
	
	@Modifying
	@Query("delete from DiskTypeSuffixOperate where componentcode=?1")
	public void deleteByComponentcode(String componentcode);
	
}

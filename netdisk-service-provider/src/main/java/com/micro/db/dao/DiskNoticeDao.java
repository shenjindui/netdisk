package com.micro.db.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskNotice;

public interface DiskNoticeDao extends JpaRepository<DiskNotice, String>,JpaSpecificationExecutor<DiskNotice>{

	@Modifying
	@Query("update DiskNotice set status=1,readtime=?2 where userid=?1 and status=0")
	public void updateReadStatus(String userid,Date readtime);
	
	@Modifying
	@Query("delete from DiskNotice where userid=?1")
	public void deleteAll(String userid);
	
	@Query("select count(1) from DiskNotice where status=0 and userid=?1")
	public int findNoticesCount(String userid);
}

package com.micro.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.LogInfo;

public interface LogInfoDao extends JpaRepository<LogInfo, String>,JpaSpecificationExecutor<LogInfo>{
	@Query("select t from LogInfo t where traceid=?1 order by starttime asc")
	public List<LogInfo> findListByTraceid(String traceid);
}

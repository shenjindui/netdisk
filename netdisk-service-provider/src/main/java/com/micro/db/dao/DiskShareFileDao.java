package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskShareFile;

public interface DiskShareFileDao extends JpaRepository<DiskShareFile, String>,JpaSpecificationExecutor<DiskShareFile>{
	@Query("select t from DiskShareFile t where shareid=?1 and pid=?2 order by createtime desc")
	public List<DiskShareFile> findListChild(String shareid,String pid);
}

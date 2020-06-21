package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskAppFile;

public interface DiskAppFileDao extends JpaRepository<DiskAppFile, String>,JpaSpecificationExecutor<DiskAppFile> {

	@Modifying
	@Query("update DiskAppFile set isbreak=1 where appid=?1 and businessid=?2 and businesstype=?3 and filemd5=?4 and delstatus=0")
	public void updateIsBreak(String appId,String businessid, String businesstype, String filemd5);
	
	@Modifying
	@Query("update DiskAppFile set delstatus=1 where id=?1")
	public void updateDelStatus(String id);

	@Query("select t from DiskAppFile t where appid=?1 and businessid=?2 and delstatus=0")
	public List<DiskAppFile> findListByBusinessid(String appid,String businessid);
	
	//判断是否已经存在
	@Query("select t from DiskAppFile t where appid=?1 and businessid=?2 and businesstype=?3 and delstatus=0")
	public List<DiskAppFile> findListByBusinessidAndBusinesstype(String appid,String businessid,String businesstype);
	
	//判断是否已经存在
	@Query("select t from DiskAppFile t where appid=?1 and businessid=?2 and businesstype=?3 and filemd5=?4 and filename=?5 and delstatus=0")
	public List<DiskAppFile> findLists(String appid,String businessid,String businesstype,String filemd5,String filename);
	
	//判断是否已经存在
	@Query("select count(1) from DiskAppFile t where appid=?1 and businessid=?2 and businesstype=?3 and filemd5=?4 and filename=?5 and delstatus=0")
	public Integer findRecordIsExist(String appid,String businessid,String businesstype,String filemd5,String filename);
	
}

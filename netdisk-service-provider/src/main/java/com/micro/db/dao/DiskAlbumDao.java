package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskAlbum;

public interface DiskAlbumDao extends JpaRepository<DiskAlbum, String>,JpaSpecificationExecutor<DiskAlbum> {

	@Query("select t from DiskAlbum t where createuserid=?1 order by createtime desc")
	public List<DiskAlbum> findListByUserid(String userid);
	
	@Modifying
	@Query("update DiskAlbum set coverurl=?2 where id=?1")
	public void updateAlbumCover(String albumid,String coverurl);
}

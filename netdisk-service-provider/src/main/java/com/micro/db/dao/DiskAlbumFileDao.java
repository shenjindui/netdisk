package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskAlbumFile;

public interface DiskAlbumFileDao extends JpaRepository<DiskAlbumFile, String>,JpaSpecificationExecutor<DiskAlbumFile> {

	@Modifying
	@Query("delete from DiskAlbumFile where albumid=?1")
	public void deleteByAlbumid(String albumid);
	
	@Modifying
	@Query("delete from DiskAlbumFile where fileid=?1")
	public void deleteByFileid(String fileid);
	
	@Query("select t from DiskAlbumFile t where albumid=?1")
	public List<DiskAlbumFile> findListByAlbumid(String albumid);
	
	@Query("select t from DiskAlbumFile t where fileid=?1")
	public DiskAlbumFile findFileIsExist(String fileid);
	
	@Query("select count(1) from DiskAlbumFile where albumid=?1 and fileid=?2")
	public Integer findFileIsInAlbum(String albumid,String fileid);
}

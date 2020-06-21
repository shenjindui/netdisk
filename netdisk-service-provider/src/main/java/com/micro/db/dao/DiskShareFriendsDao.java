package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskShareFriends;

public interface DiskShareFriendsDao extends JpaRepository<DiskShareFriends, String>,JpaSpecificationExecutor<DiskShareFriends>  {

	@Query("select t from DiskShareFriends t where shareid=?1")
	public List<DiskShareFriends> findListByShareid(String shareid);
}

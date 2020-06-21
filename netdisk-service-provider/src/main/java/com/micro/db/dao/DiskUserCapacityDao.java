package com.micro.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskUserCapacity;

public interface DiskUserCapacityDao extends JpaRepository<DiskUserCapacity, String>,JpaSpecificationExecutor<DiskUserCapacity> {

	//查询容量
	@Query("select t from DiskUserCapacity t where userid=?1")
	public DiskUserCapacity findByUserid(String userid);
	
	//删除容量
	@Modifying
	@Query("delete from DiskUserCapacity where userid=?1")
	public void deleteByUserid(String userid);

	//查询用户数
	@Query("select count(1) from DiskUserCapacity t")
	public Integer findUserNum();
	
	//新增容量
	@Modifying
	@Query("update DiskUserCapacity set totalcapacity=totalcapacity+?1 where userid=?2")
	public void addCapacity(Long totalcapacity,String userid);
	
	//新增已用容量
	@Modifying
	@Query("update DiskUserCapacity set usedcapacity=usedcapacity+?1 where userid=?2")
	public void addUsedCapacity(Long capacity,String userid);
	
	//减少已用容量
	@Modifying
	@Query("update DiskUserCapacity set usedcapacity=usedcapacity-?1 where userid=?2")
	public void reduceUsedCapacity(Long capacity,String userid);
	
	
}

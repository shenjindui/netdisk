package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskUserCapacityHistory;

public interface DiskUserCapacityHistoryDao extends JpaRepository<DiskUserCapacityHistory, String>,JpaSpecificationExecutor<DiskUserCapacityHistory>{

}

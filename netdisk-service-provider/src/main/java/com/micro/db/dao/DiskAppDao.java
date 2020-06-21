package com.micro.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.micro.model.DiskApp;

public interface DiskAppDao extends JpaRepository<DiskApp, String>,JpaSpecificationExecutor<DiskApp>{

}

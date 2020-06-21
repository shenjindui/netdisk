package com.micro.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.micro.model.DiskShareSave;

public interface DiskShareSaveDao extends JpaRepository<DiskShareSave, String>,JpaSpecificationExecutor<DiskShareSave>{

}

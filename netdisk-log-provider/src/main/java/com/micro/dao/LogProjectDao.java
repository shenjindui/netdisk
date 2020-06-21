package com.micro.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.micro.model.LogProject;

public interface LogProjectDao extends JpaRepository<LogProject, String>,JpaSpecificationExecutor<LogProject>{

}

package com.micro.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micro.model.DiskFileEdit;

public interface DiskFileEditDao extends JpaRepository<DiskFileEdit, String>,JpaSpecificationExecutor<DiskFileEdit>{

}

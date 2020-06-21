package com.micro.disk.service;

import java.util.List;

import com.micro.disk.bean.TypeBean;
import com.micro.disk.bean.TypeTree;

public interface TypeService {
	public void init();
	public List<TypeTree> findTrees();
	public List<TypeBean> findList();
}

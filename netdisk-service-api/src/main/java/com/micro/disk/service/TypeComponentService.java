package com.micro.disk.service;

import java.util.List;
import com.micro.disk.bean.TypeComponentBean;

public interface TypeComponentService {
	public List<TypeComponentBean> findList();
	public TypeComponentBean findOne(String id);
	public void save(TypeComponentBean bean);
	public void update(TypeComponentBean bean);
	public void delete(String id);
}

package com.micro.disk.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.micro.disk.user.bean.Page;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.bean.TreeJson;
import com.micro.disk.user.bean.UserBean;
import com.micro.disk.user.service.UserService;

@Service(interfaceClass=UserService.class)
@Component
public class UserServiceImpl implements UserService{

	@Override
	public Page<UserBean> findUserPageList(Integer page, Integer limit, String username, String nickname) {
		Page<UserBean> p=new Page<UserBean>();
		List<UserBean> rows=new ArrayList<UserBean>();
		
		UserBean bean1=new UserBean();
		bean1.setId("1");
		bean1.setNickname("超级管理员");
		bean1.setUsername("admin");
		bean1.setTelephone("15177917744");
		rows.add(bean1);
		
		UserBean bean2=new UserBean();
		bean2.setId("2");
		bean2.setNickname("测试账号");
		bean2.setUsername("test");
		bean2.setTelephone("13307773517");
		rows.add(bean2);
		
		
		p.setCode(0);
		p.setMsg("查询成功");
		p.setRows(rows);
		p.setTotalElements(2);
		p.setTotalPage(1);
		
		return p;
	}

	@Override
	public List<TreeJson> findUserTree(String pid, String type) {
		List<TreeJson> trees=new ArrayList<TreeJson>();
		if("root".equals(type)){
			if("0".equals(pid)){				
				TreeJson tree=new TreeJson();
				tree.setId("1");
				tree.setLabel("测试部门");
				tree.setType("org");
				tree.setDisabled(true);
				trees.add(tree);
			}
			
		}else if("org".equals(type)){
			if("1".equals(pid)){
				TreeJson tree1=new TreeJson();
				tree1.setId("2");
				tree1.setLabel("项目经理");
				tree1.setType("position");
				tree1.setDisabled(true);
				trees.add(tree1);
				
				TreeJson tree2=new TreeJson();
				tree2.setId("3");
				tree2.setLabel("开发人员");
				tree2.setType("position");
				tree2.setDisabled(true);
				trees.add(tree2);
			}
		}else if("position".equals(type)){
			if("2".equals(pid)){
				TreeJson tree=new TreeJson();
				tree.setId("1");
				tree.setLabel("超级管理员");
				tree.setType("user");
				tree.setDisabled(false);
				trees.add(tree);
				
			}else if("3".equals(pid)){
				TreeJson tree=new TreeJson();
				tree.setId("2");
				tree.setLabel("测试账号");
				tree.setType("user");
				tree.setDisabled(false);
				trees.add(tree);
			}
		}
		return trees;
	}

	@Override
	public SessionUserBean login(String username, String password) {
		if(StringUtils.isEmpty(username)){
			throw new RuntimeException("用户名不能为空");
		}
		if(StringUtils.isEmpty(password)){
			throw new RuntimeException("密码不能为空");
		}
		SessionUserBean bean=new SessionUserBean();
		if("test".equals(username)&&"test".equals(password)){
			bean.setId("2");
			bean.setNickname("测试账号");
			bean.setUsername("test");
			bean.setTelephone("13307773517");
			bean.setToken("token-test");
			return bean;
		}else if("admin".equals(username)&&"123456".equals(password)){
			bean.setId("1");
			bean.setNickname("超级管理员");
			bean.setUsername("admin");
			bean.setTelephone("15177917744");
			bean.setToken("token-admin");
			return bean;
		}else{
			throw new RuntimeException("用户名或密码错误!");
		}
	}

	@Override
	public SessionUserBean getUserByToken(String token) {
		SessionUserBean bean=new SessionUserBean();
		if("token-test".equals(token)){
			bean.setId("2");
			bean.setNickname("测试账号");
			bean.setUsername("test");
			bean.setTelephone("13307773517");
			bean.setToken("token-test");
			return bean;
		}else if("token-admin".equals(token)){
			bean.setId("1");
			bean.setNickname("超级管理员");
			bean.setUsername("admin");
			bean.setTelephone("15177917744");
			bean.setToken("token-admin");
			return bean;
		}
		return null;
	}

	@Override
	public void logout(String token) {
		
	}

}

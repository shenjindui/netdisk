package com.micro.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.bean.User;
import com.micro.common.CapacityUtils;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.disk.bean.UserCapacityHistoryBean;
import com.micro.disk.service.UserCapacityService;
import com.micro.disk.user.bean.Page;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.bean.UserBean;
import com.micro.disk.user.service.UserService;
import com.micro.mvc.UserInfoUtils;

/**
 * 用户的容量、应用分配
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {
	@Reference(check=false)
	private UserService userService;
	@Reference(check=false)
	private UserCapacityService userCapacityService;
	
	/**
	 * 用户分页列表
	 * 细节处理
	 * 1、查看哪些用户没有分配容量【核心】
	 * 	     登录之后，自动初始化容量
	 * 
	 * 2、用户删除之后，关联删除容量【不用考虑】
	 * 
	 * @param pi
	 * @param username
	 * @param nickname
	 * @return
	 */
	@RequestMapping("/findList")
	public Page<User> findList(Page<User> pi,String username,String nickname){
		try{
			//String userid,String orgid,String positionid,String roleid,String nickname,String username
			//Page<UserBean> pageInfo=userService.findUserPageList(pi.getPage(), pi.getLimit(), orgid, positionid, roleid, nickname, username);
			
			Page<UserBean> pageInfo=userService.findUserPageList(pi.getPage(), pi.getLimit(),username,nickname);
			if(pageInfo.getCode()==1){
				throw new RuntimeException("查询失败");
			}
			List<UserBean> users=pageInfo.getRows();
			List<User> rows=new ArrayList<User>();
			if(!CollectionUtils.isEmpty(users)){
				users.forEach(ub->{
					User user=new User();
					BeanUtils.copyProperties(ub, user);
					
					UserCapacityBean ucb=userCapacityService.findUserCapacity(ub.getId());
					String totalcapacity=CapacityUtils.convert(ucb.getTotalcapacity());
					String usedcapacity=CapacityUtils.convert(ucb.getUsedcapacity());
					
					user.setTotalcapacity(totalcapacity);
					user.setUsedcapacity(usedcapacity);
					rows.add(user);
				});
			}
			
			pi.setRows(rows);
			pi.setTotalElements(pageInfo.getTotalElements());
			pi.setTotalPage(pageInfo.getTotalPage());
			
			pi.setCode(0);
			pi.setMsg("查询成功");
			return pi;
		}catch(Exception e){
			pi.setCode(1);
			pi.setMsg("查询失败");
			return pi;
		}
	}
	
	@RequestMapping("/findUserHistoryCapacity")
	public com.micro.disk.bean.PageInfo<UserCapacityHistoryBean> findUserHistoryCapacity(com.micro.disk.bean.PageInfo<UserCapacityHistoryBean> pi,String userid){
		return userCapacityService.findHistory(pi.getPage(), pi.getLimit(), userid);
	}
	
	@RequestMapping("/saveUserCapacity")
	public Result saveCapacity(String userid,Long totalcapacity,String capacityunit,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			String createuserid=user.getId();
			String createusername=user.getNickname();
			
			userCapacityService.addUserCapacity(userid, totalcapacity, capacityunit,createuserid,createusername);
			return ResultUtils.success("分配容量成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

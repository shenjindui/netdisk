package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.ShareFriendsBean;

@Component
public class DiskShareFriendsJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public List<ShareFriendsBean> findFriends(String shareid){
		String sql="select dsf.userid,dsf.username,"
				+ " (select createtime from disk_share_save where shareid=dsf.shareid and userid=dsf.userid limit 0,1) as zctime "
				+ " from disk_share_friends dsf where shareid=?";
		Object[] args={
			shareid
		};
		
		List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
		List<ShareFriendsBean> rows=new ArrayList<>();
		if(!CollectionUtils.isEmpty(lists)){
			for(Map<String,Object> map:lists){
				ShareFriendsBean row=new ShareFriendsBean();
				row.setUserid(map.get("userid")==null?"":map.get("userid").toString());
				row.setUsername(map.get("username")==null?"":map.get("username").toString());
				
				String zctime=map.get("zctime")==null?"":map.get("zctime").toString();
				String zcstatus="";
				if("".equals(zctime)){
					zcstatus="未转存";
				}else{
					zcstatus="已转存";
				}
				
				row.setZcstatus(zcstatus);
				row.setZctime(zctime);
				
				rows.add(row);
			}
		}
		
		return rows;
	}
}

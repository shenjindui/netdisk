package com.micro.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.common.CapacityUtils;
import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.UserCapacityHistoryBean;

@Component
public class DiskUserCapacityHistoryJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	public PageInfo<UserCapacityHistoryBean> findListByUserid(Integer page,Integer limit,String userid){
		PageInfo<UserCapacityHistoryBean> pageInfo=new PageInfo<>();
		try{
			
			String sql="select * from disk_user_capacity_history where userid=? order by createtime desc ";
			Object[] args={
					userid
			};
			PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
			List<UserCapacityHistoryBean> rows=new ArrayList<UserCapacityHistoryBean>();
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					UserCapacityHistoryBean row=new UserCapacityHistoryBean();
					row.setUsername(map.get("createusername")==null?"":map.get("createusername").toString());
					row.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
					
					String type=map.get("type")==null?"1":map.get("type").toString();
					String capatiy=CapacityUtils.convertDetail(Long.parseLong(map.get("capacity")==null?"0":map.get("capacity").toString()));
					if("0".equals(type)){
						capatiy="-"+capatiy;
					}else{
						capatiy="+"+capatiy;						
					}
					row.setCapacity(capatiy);
					row.setLeftcapacity(CapacityUtils.convertDetail(Long.parseLong(map.get("leftcapacity")==null?"0":map.get("leftcapacity").toString())));
					
					row.setRemark(map.get("remark")==null?"":map.get("remark").toString());
					rows.add(row);
				}
			}
			pageInfo.setRows(rows);;
			pageInfo.setPage(page);
			pageInfo.setLimit(limit);
			pageInfo.setTotalElements(pi.getTotalElements());
			pageInfo.setTotalPage(pi.getTotalPage());
			pageInfo.setCode(0);
			pageInfo.setMsg("查询成功");
			
		}catch(Exception e){
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
		}
		return pageInfo;
	}
}

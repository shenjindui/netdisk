package com.micro.jdbc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.micro.model.LogInfo;
import com.micro.model.PageInfo;

@Component
public class LogInfoJdbc {
	@Autowired
	private IJdbcTemplate jdbcTemplate;
	
	/**
	 * 日志追踪分页列表
	 * @param page
	 * @param limit
	 * @param projectname
	 * @param targetmethod
	 * @param username
	 * @param starttime
	 * @param endtime
	 * @param orderfield
	 * @param ordertype
	 * @return
	 */
	public PageInfo<LogInfo> findList(Integer page, Integer limit, String projectname, String targetmethod,
			String username, String starttime, String endtime,String orderfield,String ordertype){
		PageInfo<LogInfo> pageInfo=new PageInfo<>();
		try{
			
			String sql="select "
					+ " id,traceid,requestip,userid,username,projectname,projectdesc,targetmethod,targetparams,comsumetime,returnresult,executeresult,remark,"
					+ "date_format(starttime,'%Y-%m-%d %H:%i:%S') as starttime,"
					+ "date_format(endtime,'%Y-%m-%d %H:%i:%S') as endtime "
					+ "from log_info where projectname='"+projectname+"' ";//不能写where 1=1，全表扫描
			
			//查询条件
			if(!StringUtils.isEmpty(targetmethod)){
				sql+=" and targetmethod='"+targetmethod+"'";
			}
			if(!StringUtils.isEmpty(username)){
				sql+=" and username='"+username+"'";
			}
			if(!StringUtils.isEmpty(starttime)){
				sql+=" and date_format(starttime,'%Y-%m-%d %H:%i:%S') >= '"+starttime+"'";
			}
			if(!StringUtils.isEmpty(endtime)){
				sql+=" and date_format(endtime,'%Y-%m-%d %H:%i:%S') <= '"+endtime+"'";
			}
			
			
			//排序条件
			if(StringUtils.isEmpty(orderfield)){			
				sql+=" order by starttime desc ";
			}else{
				ordertype=ordertype.replace("ending", "");
				sql+=" order by "+orderfield+" "+ordertype+" ";
			}
			
			Object[] args={
					
			};
			PageInfo<Map<String,Object>> pi= jdbcTemplate.findPageList(sql, args, page, limit);
			List<LogInfo> rows=new ArrayList<LogInfo>();
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!CollectionUtils.isEmpty(pi.getRows())){
				for(Map<String,Object> map:pi.getRows()){
					LogInfo row=new LogInfo();
					row.setId(map.get("id")==null?"":map.get("id").toString());
					row.setTraceid(map.get("traceid")==null?"":map.get("traceid").toString());
					row.setRequestip(map.get("requestip")==null?"":map.get("requestip").toString());
					row.setUserid(map.get("userid")==null?"":map.get("userid").toString());
					row.setUsername(map.get("username")==null?"":map.get("username").toString());
					row.setProjectname(map.get("projectname")==null?"":map.get("projectname").toString());
					row.setProjectdesc(map.get("projectdesc")==null?"":map.get("projectdesc").toString());
					row.setTargetmethod(map.get("targetmethod")==null?"":map.get("targetmethod").toString());
					row.setTargetparams(map.get("targetparams")==null?"":map.get("targetparams").toString());
					row.setStarttime(df.parse(map.get("starttime").toString()));
					row.setEndtime(df.parse(map.get("endtime").toString()));
					row.setComsumetime(Long.parseLong(map.get("comsumetime")==null?"0":map.get("comsumetime").toString()));
					row.setReturnresult(map.get("returnresult")==null?"":map.get("returnresult").toString());
					row.setExecuteresult(map.get("executeresult")==null?"":map.get("executeresult").toString());
					row.setRemark(map.get("remark")==null?"":map.get("remark").toString());
					rows.add(row);
				}
			}
			
			pageInfo.setCode(0);
			pageInfo.setMsg("查询成功");
			pageInfo.setPage(page);
			pageInfo.setLimit(limit);
			pageInfo.setRows(rows);
			pageInfo.setTotalElements(pi.getTotalElements());
			pageInfo.setTotalPage(pi.getTotalPage());
			
		}catch(Exception e){
			pageInfo.setCode(1);
			pageInfo.setMsg("查询失败");
		}
		return pageInfo;
	}
}

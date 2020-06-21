package com.micro.logs.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDiff {
	public static long getDiff(String startTime,String endTime){
		try{			
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(startTime==null||"".equals(startTime)){
				throw new RuntimeException("开始时间不能为空");
			}
			if(endTime==null||"".equals(endTime)){
				throw new RuntimeException("结束时间不能为空");
			}
			
			Date start=format.parse(startTime);
			Date end=format.parse(endTime);
			long diff=end.getTime()-start.getTime();
			
			return diff;
		}catch(Exception e){
			return 0;
		}
	}
}

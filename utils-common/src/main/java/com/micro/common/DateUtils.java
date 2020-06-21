package com.micro.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
	
	/**
	 * 计算耗时
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String getDiff(String startTime,String endTime){
		try{			
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(startTime==null||"".equals(startTime)){
				return "";
			}
			if(endTime==null||"".equals(endTime)){
				return "";
			}
			
			Date start=format.parse(startTime);
			Date end=format.parse(endTime);
			
			long diff=end.getTime()-start.getTime();
			
			//1、如果小于60*1000秒，则返回秒
			//2、如果大于60*1000秒，小于1*60*60*1000，则返回分
			//3、如果大于1*60*60*100，小于24*60*60*1000，则返回小时
			//4、否则返回天
			
			long dayUnit = 1000 * 24 * 60 * 60;
			long hourUnit = 1000 * 60 * 60;
			long minUnit = 1000 * 60;
			long secondUnit = 1000;
			 
			long day = diff / dayUnit;
			long hour = diff % dayUnit / hourUnit;
			long min = diff % dayUnit % hourUnit / minUnit;
			long second = diff % dayUnit % hourUnit % minUnit / secondUnit;
			
			if(diff<(60*1000)){
				return second+"秒";
				
			}else if((60*1000)<diff&&diff<(1*60*60*1000)){
				return min+"分"+second+"秒";
			}else if((1*60*60*1000)<diff&&diff<(24*60*60*1000)){
				
				return hour+"时"+min+"分";
			}else{
				return day+"天"+hour+"时";
			}
		}catch(Exception e){
			return "";
		}
	}
	
    //根据日期获取所有的时间
    public static List<String> getTimeByDate(String date){
    	try{    		
    		List<String> times=new ArrayList<String>();
    		for(int i=1;i<=24;i++){
    			if(i<10){
    				String time=date+" 0"+i+":00";
    				times.add(time);
    			}else{
    				String time=date+" "+i+":00";
    				times.add(time);
    			}
    		}
    		
    		return times;
    	}catch(Exception e){
    		throw new RuntimeException(e.getMessage());
    	}
    }
    //根据本周获取所有的日期
    public static List<String> getDaysByWeek(){
    	try{    		
    		List<String> dates=new ArrayList<String>();
    		String first=getWeekStart();
    		dates.add(first);
    		
    		String date=first;
    		for(int i=1;i<=6;i++){
    			Date datePar=parseDate(date, "yyyy-MM-dd");
    			Date nextDate=getSpecialDate(datePar,1);
    			String dateFormat=formatDate(nextDate, "yyyy-MM-dd");
    			dates.add(dateFormat);
    			date=dateFormat;
    		}
    		
    		return dates;
    	}catch(Exception e){
    		throw new RuntimeException(e.getMessage());
    	}
    }
    //根据月份获取所有的天
    public static List<String> getDaysByMonth(String month){
    	try{    		
    		String date=month+"-01";
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(sdf.parse(date));
    		int num=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    		
    		List<String> days=new ArrayList<String>();
    		for(int i=1;i<=num;i++){
    			String day="";
    			if(i<10){
    				day=month+"-0"+i;    				
    			}else{
    				day=month+"-"+i;    				
    			}
    			days.add(day);
    		}
    		
    		return days;
    	}catch(Exception e){
    		throw new RuntimeException(e.getMessage());
    	}
    }
    
	/**
	 * 获取星期几
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	/**
	 * 获取当前格式化时间
	 * @author 郑伟业
	 * 2018年10月26日
	 * @return
	 */
	public static String getCurrentTime(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sdf.format(date);
	}
	
	/**
	 * 自定义日期格式化
	 * @author 郑伟业
	 * 2018年10月26日
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date,String format){
		if(date==null){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 自定义日期格式化
	 * @author 郑伟业
	 * 2018年10月26日
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parseDate(String date,String format){
		try{			
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(date);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 根据截止日期计算当前状态
	 * 
	 * @author 郑伟业
	 * 2018年10月26日
	 * @param time
	 * @return 0正常，1即将过期，2已逾期
	 */
	public static String getTimeStatus(Object time){
		try{
			if(time==null){
				return "0";
			}
			SimpleDateFormat formmat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date=formmat.parse(time.toString());
			
			if(new Date().before(date)){
				return "0";
			}else{
				return "2";
			}
		}catch(Exception e){
			return "0";
			//throw new RuntimeException("time格式不对，转换出错");
		}
	}
	
	/**
	 * 判断字符串是否是指定的日期格式
	 * @author 钟业剑
	 * 2018年11月8日 上午9:28:23
	 * @param str 字符串的日期
	 * @param format 要判断的日期格式，比如：yyyy-MM-dd hh:mm:ss
	 * @return boolean
	 */
	public static boolean isValidDate(String str, String format) {
		boolean convertSuccess = true;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			sdf.setLenient(false);
			sdf.parse(str);
		} catch (Exception e) {
			convertSuccess = false;
		} 
		return convertSuccess;
	}
	
	/**
	 * 比较字符串的时间，
	 * @author 钟业剑
	 * 2018年11月8日 上午10:43:33
	 * @param bDate 开始时间
	 * @param eDate 结束时间
	 * @param format 时间的格式
	 */
	public static boolean comparisonSize(String bDate, String eDate, String format){
		try {
			SimpleDateFormat sdf=new SimpleDateFormat(format); 
			Date bt = sdf.parse(bDate);
			Date et=sdf.parse(eDate); 
			if (bt.before(et)){ 
				return true;
			}else{ 
				return false;
			} 
		} catch (ParseException e) {
			e.printStackTrace();
			return false; 
		}
	}
	
	/**
	 * 获取n天之前  或者  获取n天之后
	 * @param n
	 * @return
	 */
	public static Date getSpecialDate(Integer n){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, n);
		return calendar.getTime();
	}
	public static Date getSpecialDate(Date date,Integer n){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, n);
		return calendar.getTime();
	}
	public static String  getAfterMonth(String inputDate,int monthNum) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例   
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
        Date date = null;   
        try{   
            date = sdf.parse(inputDate);//初始日期   
        }catch(Exception e){  

        }   
        c.setTime(date);//设置日历时间   
        c.add(Calendar.MONTH,monthNum);//在日历的月份上增加6个月
        String strDate = sdf.format(c.getTime());//的到你想要得6个月后的日期   
        return strDate;
	}
	
	
	/**
	 * 获取当前日期
	 */
	public static String getToday(){
		
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date);
		return today;
		
	}
	
	/**
	 * 本周第一天日期
	 */
	public static String getWeekStart(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date date = cal.getTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String firstDay = sdf.format(date);
		return firstDay;
		
	}
	
	/**
	 * 本周最后一天日期
	 */
	public static String getWeekEnd(){
		Calendar curStartCal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = (Calendar) curStartCal.clone();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DATE, 6);
		String lastDay = df.format(cal.getTime());
		return lastDay;
	}
	
	/**
	 * 本月第一天日期
	 */
	public static String getMonthStart(){
		Calendar curStartCal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal2 = (Calendar) curStartCal.clone();
		cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMinimum(Calendar.DAY_OF_MONTH));
		String firstDay = format.format(cal2.getTime());
		return firstDay;
		
	}
	
	/**
	 * 本月最后一天日期
	 */
	public static String getMonthEnd(){
		Calendar curStartCal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal2 = (Calendar) curStartCal.clone();
		cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
		String lastDay = format.format(cal2.getTime());
		return lastDay;
		
	}
	
	/**
	 * 今年第一天日期
	 */
	public static String getYearStart(){
		Calendar currCal=Calendar.getInstance();  
        int year = currCal.get(Calendar.YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR,year);
		Date date = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String firstDay = format.format(date);
		return firstDay;
	}
	
	/**
	 * 今年最后第一天日期
	 */
	public static String getYearEnd(){
		Calendar currCal=Calendar.getInstance();  
        int year = currCal.get(Calendar.YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR,year);
		calendar.roll(Calendar.DAY_OF_YEAR,-1);
		Date date = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String lastDay = format.format(date);
		return lastDay;
		
	}
	/**
	 * 计算相差天数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int caculateTotalDay(String startTime,String endTime) {
        SimpleDateFormat formatter =new SimpleDateFormat( "yyyy-MM-dd");
        Date date1=null;
        Date date = null;
        Long l = 0L;
        try {
            date = formatter.parse(startTime);
            long ts = date.getTime();
            
            date1 =  formatter.parse(endTime);
            long ts1 = date1.getTime();

            l = (ts1-ts) / (1000 * 60 * 60 * 24);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l.intValue();
    }
}

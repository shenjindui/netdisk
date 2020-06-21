package com.micro.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.util.StringUtils;

public class CapacityUtils {
	public static String convertDetail(Long size){
		if(size==0){
			return "0B";
		}
		if(size<1024){
			return size+"B";
		}else if(1.0*1024<=size&&size<1.0*1024*1024){//1KB<=size<1M
			return formatForInteger(size/(1.0*1024))+"KB";
		}else if(1.0*1024*1024<=size&&size<1.0*1024*1024*1024){
			return formatForInteger(size/(1.0*1024*1024))+"MB";
		}else if(1.0*1024*1024*1024<=size&&size<1.0*1024*1204*1024*1024){
			return formatForInteger(size/(1.0*1024*1024*1024))+"GB";
		}else{
			return formatForInteger(size/(1.0*1024*1024*1024*1024))+"TB";
		}
	}
	public static String convert(Long size){
		//小于1M
		//大于1M并且小于1G
		//大于1G并且小于1T
		//大于1T
		if(size==null){
			return "0B";
		}
		if(size==0){
			return "0B";
		}
		if(size<1024){
			return size+"B";
		}else if(1.0*1024<=size&&size<1.0*1024*1024){//1KB<=size<1M
			return formatDouble(size/(1.0*1024))+"KB";
		}else if(1.0*1024*1024<=size&&size<1.0*1024*1024*1024){
			return formatDouble(size/(1.0*1024*1024))+"MB";
		}else if(1.0*1024*1024*1024<=size&&size<1.0*1024*1204*1024*1024){
			return formatDouble(size/(1.0*1024*1024*1024))+"GB";
		}else{
			return formatDouble(size/(1.0*1024*1024*1024*1024))+"TB";
		}
	}
	public static String convert(String filesize){
		if(StringUtils.isEmpty(filesize)){
			return "-";
		}
		long size=Long.parseLong(filesize);
		if(size==0){
			return "-";
		}
		String str="";
		if(size<1024){
			str= size+"B";
		}else if(1.0*1024<=size&&size<1.0*1024*1024){//1KB<=size<1M
			str= formatDouble(size/(1.0*1024))+"KB";
		}else if(1.0*1024*1024<=size&&size<1.0*1024*1024*1024){
			str= formatDouble(size/(1.0*1024*1024))+"MB";
		}else if(1.0*1024*1024*1024<=size&&size<1.0*1024*1204*1024*1024){
			str= formatDouble(size/(1.0*1024*1024*1024))+"GB";
		}else{
			str= formatDouble(size/(1.0*1024*1024*1024*1024))+"TB";
		}
		return str;
	}
	
	public static String formatDouble(double d) {
	    BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
	    double num = bg.doubleValue();
	    if (Math.round(num) - num == 0) {
	        return String.valueOf((long) num);
	    }
	    return String.valueOf(num);
	}
	public static String formatForInteger(double d) {
		BigDecimal bg = new BigDecimal(d).setScale(100000, RoundingMode.UP);
		double num = bg.doubleValue();
		if (Math.round(num) - num == 0) {
			return String.valueOf((long) num);
		}
		return String.valueOf(num);
	}
}

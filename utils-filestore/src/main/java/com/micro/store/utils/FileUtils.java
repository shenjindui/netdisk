package com.micro.store.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.springframework.util.StringUtils;


public class FileUtils {
	/**
	 * 获取对应的contentType
	 * @param fileName
	 * @return
	 */
	public static String getContentType(String fileName){
        String ext = "";
        String contentType="";
        //获取后缀
        if(!"".equals(fileName) && fileName.contains(".")){
            ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        }
        
        //类型
        if ("png".equals(ext) || "jpeg".equals(ext) || "jpg".equals(ext)) {
        	contentType="image/jpeg";
        }
        if ("bmp".equals(ext)) {
        	contentType="image/bmp";
        }
        if ("pdf".equals(ext)) {
        	contentType="application/pdf";
        }
		return contentType;
	}
	/**
	 * 判断附件是否是图片
	 * @param filename
	 * @return
	 */
	public static boolean isImage(String filename){
		String suff=filename.substring(filename.lastIndexOf(".")+1);//后缀名
		suff=suff.toLowerCase();
		if("jpg".equals(suff)||"jpeg".equals(suff)||"gif".equals(suff)||"png".equals(suff)){
			return true;
		}else{
			return false;
		}
	}
	public static String getFolder(){
		SimpleDateFormat formatYear=new SimpleDateFormat("yyyy");
		SimpleDateFormat formatMonth=new SimpleDateFormat("MM");
		SimpleDateFormat formatDay=new SimpleDateFormat("dd");
		String year=formatYear.format(new Date());
		String month=formatMonth.format(new Date());
		String day=formatDay.format(new Date());
		
		String folder=year+"/"+month+"/"+day;
		return folder;
	}
	
	/**
	 * 获取真实名称
	 * @author 郑伟业
	 * 2018年10月22日
	 * @param filename
	 * @return
	 */
	public static String getRealName(String fileName){
		if(fileName==null||"".equals(fileName)){
			throw new RuntimeException("fileName不能为空");
		}else{
			if(!fileName.contains(".")){
				throw new RuntimeException("fileName格式不对");
			}
		}
		fileName=fileName.replaceAll("\\\\","/");
		if(fileName.contains("/")){
			String[] names=fileName.split("/");
			fileName=names[names.length-1];
		}
		return fileName;
	}
	/**
	 * 获取存储文件系统的真实名称
	 * @author 郑伟业
	 * 2018年10月22日
	 * @param filename
	 * @return
	 */
	public static String getSaveName(String filename){
		String suff=filename.substring(filename.lastIndexOf(".")+1);//后缀名
		return UUID.randomUUID().toString()+"."+suff;
	}
	
	/**
	 * 获取转换后缀名称
	 * @param filename
	 * @param convertSuffix
	 * @return
	 */
	public static String getConvertName(String filename,String convertSuffix){
		String name=filename.substring(0, filename.lastIndexOf("."));
		return name+"."+convertSuffix;
	}
	
	/**
	 * 获取文件-后缀
	 * @author 郑伟业
	 * 2018年10月21日
	 * @param filename
	 * @return
	 */
	public static String getFileSuffix(String filename){
		String suff=filename.substring(filename.lastIndexOf(".")+1);//后缀名
		suff=suff.toLowerCase();
		return suff;
	}
	
	/**
	 * @category 获取附件大小
	 * @param l
	 * @return
	 */
	public static String getFileSize(long l){
		long d=l/1024; 
		if(d<1024){
			return d+"kb";
		}else if(d>1024&&d<1024*1024){
			return d/1024+"mb";
		}else{
			return d/(1024*1024)+"gb";
		}
	}
}

package com.micro.common.file;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ByteResponse {
	public static void writePdf(HttpServletRequest request,HttpServletResponse response,byte[] bytes,String fileName){
		try{
			response.setHeader("Content-Type", "application/pdf;charset=UTF-8");
			response.setHeader("Content-Disposition", "inline;filename="+fileName);
			
			InputStream is = new ByteArrayInputStream(bytes);
    		byte[] bs = new byte[1024];
    		int len;
    		
    		OutputStream os=response.getOutputStream();
    		while ((len = is.read(bs)) != -1) {
    			os.write(bs, 0, len);
    			os.flush();
    		}
    		os.close();
    		is.close();
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	public static void writeImage(HttpServletRequest request,HttpServletResponse response,byte[] bytes){
		try{
			//response.setContentType("application/pdf;charset=UTF-8");
			InputStream is = new ByteArrayInputStream(bytes);//byte[]转inputstream
			byte[] bs = new byte[1024];
			int len;
			
			OutputStream os=response.getOutputStream();
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				os.flush();
			}
			os.close();
			is.close();
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static void writeAttachment(HttpServletRequest request,HttpServletResponse response,byte[] bytes,String fileName){
		try{
			InputStream is = new ByteArrayInputStream(bytes);//byte[]转inputstream
			byte[] bs = new byte[1024];
			int len;
			
			String userAgent = request.getHeader("User-Agent");
			//针对IE或者以IE为内核的浏览器：
			if (userAgent.contains("MSIE")||userAgent.contains("Trident")) {
				fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			} else {
				//非IE浏览器的处理：
				fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
			}
			
			//通过设置response的响应头就可以实现，content-disposition服务器通过这个响应头告诉浏览器通过下载方式打开文件。
			//response.setContentType("application/vnd.ms-excel; charset=utf-8");
			//response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(""+fileName+"", "UTF-8")); 
			response.setHeader("content-disposition", "attachment;filename=" + fileName); 
			
			OutputStream oss=response.getOutputStream();
			while ((len = is.read(bs)) != -1) {
				oss.write(bs, 0, len);
				oss.flush();
			}
			oss.close();
			is.close();
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	public static void write(HttpServletRequest request,HttpServletResponse response,byte[] bytes){
		try{
			InputStream is = new ByteArrayInputStream(bytes);//byte[]转inputstream
			byte[] bs = new byte[1024];
			int len;
			OutputStream oss=response.getOutputStream();
			while ((len = is.read(bs)) != -1) {
				oss.write(bs, 0, len);
				oss.flush();
			}
			oss.close();
			is.close();
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}

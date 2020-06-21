package com.micro.netdisk.javasdk.fileutils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;

/**
 * 计算大文件的MD5
 * @author Administrator
 *
 */
public class FileMd5Utils {
	private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
	
	public static void main(String[] args) {
		//File file=new File("E:/test/咕泡AI20200109公开课--K-means实现图像分割（Aaron老师）.mp4");
		//File file=new File("E:/test/青山_20200201_分布式架构核心技术之消息队列.mp4");
		File file=new File("E:/test/aspose-words-15.8.0-jdk16.jar");
		String md5=FileMd5Utils.calcMD5(file);
		System.out.println(md5);
	}
	
	public static String calcMD5(File file) {
		InputStream stream =null;
		try {
			stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ);
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] buf = new byte[1024];
			int len;
			while ((len = stream.read(buf)) > 0) {
				digest.update(buf, 0, len);
			}
			return toHexString(digest.digest()).toLowerCase();
		} catch (Exception e) {
			return "";
		}finally{
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String toHexString(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);
		for (byte b : data) {
			r.append(hexCode[(b >> 4) & 0xF]);
			r.append(hexCode[(b & 0xF)]);
		}
		return r.toString();
	}
}

package com.micro.netdisk.javasdk.thread;

import java.io.File;
import java.util.concurrent.Callable;

import com.micro.netdisk.javasdk.fileutils.FileMd5Utils;

/**
 * 计算MD5
 * @author Administrator
 *
 */
public class Md5CalThread implements Callable<String>{
	private File file;
	public Md5CalThread(File file){
		this.file=file;
	}
	@Override
	public String call() throws Exception {
		try{			
			String trustFileMd5=FileMd5Utils.calcMD5(file);
			return trustFileMd5;
		}catch(Exception e){
			return "";
		}
	}

}

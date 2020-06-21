package com.micro.netdisk.javasdk.thread;

import java.io.File;
import java.util.concurrent.Callable;
import com.micro.netdisk.javasdk.fileutils.FileMd5Utils;
import com.micro.netdisk.javasdk.proxy.RemoteApi;
import com.micro.netdisk.javasdk.proxy.RemoteApiProxy;

/**
 * 如果文件大于500M，则开启线程计算MD5，并且做比较
 * @author Administrator
 *
 */
@Deprecated//不鼓励使用的程序元素，通常是因为它很危险，或者是因为存在更好的替代方案
public class Md5Thread implements Callable<Integer>{
	private File file;
	private String appId;
	private String fileMd5;
	private String businessid;
	private String businesstype;
	public volatile boolean mainThreadIsOver=false;//一定要加volatile关键字
	
	public Md5Thread(File file,String appId,String businessid,String businesstype,String fileMd5){
		this.file=file;
		this.appId=appId;
		this.businessid=businessid;
		this.businesstype=businesstype;
		this.fileMd5=fileMd5;
	}
	
	//删除本地文件（情况一：计算md5的线程  快于  切块上传---主线程删除文件；情况二：计算md5的线程  慢于  切块上传---子线程程删除文件）
	@Override
	public Integer call() throws Exception {
		try{			
			//1.计算md5
			String trustFileMd5=FileMd5Utils.calcMD5(file);
			//Thread.sleep(1000*60);
			//2.校验md5，并且发现MD5不一致
			if(!fileMd5.equals(trustFileMd5)){
				if(mainThreadIsOver){
					//主线程执行完成，子线程没有执行完成，由子线程去调用接口标准有损坏
					RemoteApi remoteApi=RemoteApiProxy.getInstance();
					remoteApi.fileHasBreak(appId, businessid, businesstype, fileMd5);
					
				}else{
					//主线程未执行完成，子线程已执行完成，则子线程及时通知主线程停止上传并且返回错误
					throw new FileIsBreakException("文件有损坏");
				}
			}
			System.out.println("子线程执行完成......................................");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//3.主线程执行完成，子删除文件
			if(mainThreadIsOver){
				if(file!=null&&file.exists()){				
					file.delete();
				}
			}
		}
		return 1;
	}

}

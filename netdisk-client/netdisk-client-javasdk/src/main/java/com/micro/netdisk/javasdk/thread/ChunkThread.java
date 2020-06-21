package com.micro.netdisk.javasdk.thread;

import java.util.concurrent.CountDownLatch;
import com.micro.netdisk.javasdk.proxy.RemoteApi;
import com.micro.netdisk.javasdk.proxy.RemoteApiProxy;

/**
 * 切块上传
 * @author Administrator
 *
 */
@Deprecated
public class ChunkThread implements Runnable{
	public volatile static boolean isStop=false;//是否强行停止
	
	private byte[] bytes;
	private String appId;
	private String filemd5;
	private String filename;
	private long chunknum;
	private String userid;
	private CountDownLatch cdl;
	public ChunkThread(CountDownLatch cdl,byte[] bytes,String appId,String filemd5,String filename,long chunknum,String userid){
		this.bytes=bytes;
		this.appId=appId;
		this.filemd5=filemd5;
		this.filename=filename;
		this.chunknum=chunknum;
		this.userid=userid;
		this.cdl=cdl;
	}
	@Override
	public void run() {
		try{
			//切块上传
			RemoteApi remoteApi=RemoteApiProxy.getInstance();
			remoteApi.uploadChunk(bytes, appId, filemd5, filename, chunknum, userid);
			
		}catch(Exception e){
			//其中一块上传失败，则没有必要往下传了
			e.printStackTrace();
		}finally{
			//执行完成减一，所有线程减完，主线程才往下执行
			cdl.countDown();			
		}
	}

}

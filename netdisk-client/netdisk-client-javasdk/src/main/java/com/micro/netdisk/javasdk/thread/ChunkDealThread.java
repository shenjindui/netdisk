package com.micro.netdisk.javasdk.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import com.micro.netdisk.javasdk.proxy.RemoteApi;
import com.micro.netdisk.javasdk.proxy.RemoteApiProxy;

public class ChunkDealThread implements Callable<Integer>{
	private byte[] bytes;
	private String appId;
	private String filemd5;
	private String filename;
	private long chunknum;
	private String userid;
	private CountDownLatch cdl;
	
	public ChunkDealThread(CountDownLatch cdl,byte[] bytes,String appId,String filemd5,String filename,long chunknum,String userid){
		this.cdl=cdl;
		this.bytes=bytes;
		this.appId=appId;
		this.filemd5=filemd5;
		this.filename=filename;
		this.chunknum=chunknum;
		this.userid=userid;
	}
	
	@Override
	public Integer call() throws Exception {
		try{
			//切块上传
			RemoteApi remoteApi=RemoteApiProxy.getInstance();
			remoteApi.uploadChunk(bytes, appId, filemd5, filename, chunknum, userid);
			return 1;
		}catch(Exception e){
			return 0;
		}finally{
			cdl.countDown();
		}
	}
}

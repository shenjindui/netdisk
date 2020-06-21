package com.micro.netdisk.javasdk.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ChildThread implements Callable<Integer>{
	private long time;
	private CountDownLatch cdl;
	private TestBean tb;
	public ChildThread(CountDownLatch cdl,long time,TestBean tb){
		this.cdl=cdl;
		this.time=time;
		this.tb=tb;
	}
	@Override
	public Integer call() throws Exception {
		try{			
			Thread.sleep(time*1000);
			if(time==5){
				throw new RuntimeException(Thread.currentThread().getName()+"出错!!!");
			}
			System.out.println(Thread.currentThread().getName()+"--处理完成");
			return 1;
		}catch(Exception e){
			System.out.println(e.getMessage());
			tb.setIserror(true);
			return 0;
		}finally{
		}
	}

}

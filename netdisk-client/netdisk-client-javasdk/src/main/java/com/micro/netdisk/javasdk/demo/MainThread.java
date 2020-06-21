package com.micro.netdisk.javasdk.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

//1.模仿其中一个失败
//2.模仿全部成功才往下执行
public class MainThread {
	public static void main(String[] args)throws Exception {
		try{
			int num=50;
			CountDownLatch cdl=new CountDownLatch(num);
			TestBean tb=new TestBean();
			tb.setIserror(false);
			List<FutureTask<Integer>> tasks=new ArrayList<>();
			for(int i=1;i<=num;i++){				
				ChildThread ct=new ChildThread(cdl,i,tb);
				FutureTask<Integer> futureTask=new FutureTask<Integer>(ct);
				new Thread(futureTask).start();
				futureTask.get();
			}
			
		    System.out.println("主线程执行成功.....................");
		}catch(Exception e){
			//e.printStackTrace();
		}
	}
}

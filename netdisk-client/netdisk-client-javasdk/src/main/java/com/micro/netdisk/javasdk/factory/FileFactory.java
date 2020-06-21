package com.micro.netdisk.javasdk.factory;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.micro.netdisk.javasdk.balance.HostBean;
import com.micro.netdisk.javasdk.service.FileService;
import com.micro.netdisk.javasdk.service.FileServiceImpl;
import com.micro.netdisk.javasdk.thread.HeartBeatThread;

/**
 * 工厂类
 * 1、创建FileService
 * 2、启动线程监听服务是否存活
 * @author Administrator
 *
 */
public class FileFactory {
	private volatile static FileService fs;
	public volatile static Set<HostBean> hosts=new HashSet<>();//不可重复
	public volatile static Set<HostBean> activeHosts=new HashSet<>();//可不重复
	public volatile static String balance;
	public volatile static String mode=TransprotMode.HTTP_MODE;
	private volatile static ScheduledExecutorService heartBeanEs=null;
	/**
	 * 创建服务
	 * @param hosts
	 * @return
	 */
	public static FileService createFileService(List<HostBean> hosts){
		
		return createFileService(hosts, BalanceMode.BALANCE_RANDOM);
	}
	/**
	 * 创建服务
	 * @param hosts 服务地址
	 * @param balance 负载均衡策略
	 * @return
	 */
	public static FileService createFileService(List<HostBean> hosts,String balance){
		try {
			if(hosts==null||hosts.size()==0){
				throw new RuntimeException("请传递网盘服务端的地址");
			}else{				
				FileFactory.hosts.addAll(hosts);
				FileFactory.activeHosts.addAll(hosts);
			}
			//负载策略
			if(balance==null||"".equals(balance)){
				balance=BalanceMode.BALANCE_ROUNDBIN;
			}else{
				if(!balance.equals(BalanceMode.BALANCE_RANDOM)&&!balance.equals(BalanceMode.BALANCE_ROUNDBIN)
						&&!balance.equals(BalanceMode.BALANCE_ROUNDBIN)&&!balance.equals(BalanceMode.BALANCE_ROUNDBINWEIGHT)){
					throw new RuntimeException("负载策略不存在");
				}
				FileFactory.balance=balance;
			}
			//单例模式
			if(fs==null){
				synchronized (FileFactory.class) {
					if(fs==null){	
						//创建实例
						//Class<?> driverImplClass = Class.forName("com.micro.netdisk.javasdk.service.FileServiceImpl");
						//fs = (FileService) driverImplClass.newInstance();
						
						fs=new FileServiceImpl();
						
						//启动一个线程来监听hosts，做到动态感知
						heartBeanEs=Executors.newScheduledThreadPool(1);
						//延迟1s，每隔5s检查一次
						heartBeanEs.scheduleWithFixedDelay(new HeartBeatThread(), 1,5, TimeUnit.SECONDS);
					}
				}
			}
            return fs;
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
	}
}
/*
 
if(mode==null||"".equals(mode)){
	mode=TransprotMode.HTTP_MODE;
}else{
	if(!mode.equals(TransprotMode.HTTP_MODE)&&!mode.equals(TransprotMode.TCP_MODE)){
		throw new RuntimeException("通讯模式不存在");
	}
	mode=mode;
}
*/
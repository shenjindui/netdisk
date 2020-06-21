package com.micro.netdisk.javasdk.thread;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Set;

import com.micro.netdisk.javasdk.balance.HostBean;
import com.micro.netdisk.javasdk.factory.FileFactory;

/**
 * 心跳检查服务器是否存活
 * @author Administrator
 *
 */
public class HeartBeatThread implements Runnable{

	@Override
	public void run() {
		try {
			Set<HostBean> sets=FileFactory.hosts;
			if(!sets.isEmpty()){
				Iterator<HostBean> it = sets.iterator();
				while(it.hasNext()){
					HostBean bean=it.next();	
					Socket s = new Socket();
					try{						
						SocketAddress add = new InetSocketAddress(bean.getIp(), bean.getPort());
						s.connect(add, 1000);// 超时1秒
						FileFactory.activeHosts.add(bean);
						//System.out.println("检查成功-----"+FileFactory.activeHosts.size());
					}catch(Exception e){
						FileFactory.activeHosts.remove(bean);
						//System.out.println("检查失败-----"+FileFactory.activeHosts.size());
					}finally{
						s.close();
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

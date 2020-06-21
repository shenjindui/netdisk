package com.micro.lock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class LockZookeeper implements Lock {
	private CountDownLatch latch = new CountDownLatch(1);
	private static volatile ZkClient zkClient =null;
	private static String ROOT="/locks";
	
	private LockZookeeper(){}
	public static LockZookeeper getInstance(String url){
		//保证zkClient是单例
		synchronized (LockZookeeper.class) {
			if(zkClient==null){		
				zkClient = new ZkClient(url, 30000);
				boolean b=zkClient.exists(ROOT);
				if(b==false){
					zkClient.create(ROOT, "", CreateMode.PERSISTENT);
				}
			}
		}
		//LockZookeeper是多例
		return new LockZookeeper();
	}
	
	//加锁
	@Override
	public void getLock(String lockname) {
		try{			
			boolean result=tryLock(lockname);
			if (!result) {
				// 等待
				waitLock(lockname);
				// 重写获取锁的资源
				getLock(lockname);
			}
		}catch(Exception e){
			
		}
	}
	private boolean tryLock(String lockname) {
		try {
			zkClient.createEphemeral(ROOT+"/"+lockname);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 释放锁
	@Override
	public void unLock(String lockname) {
		try{			
			zkClient.delete(ROOT+"/"+lockname);
			/*if (zkClient != null) {
				zkClient.close();
			}*/
		}catch(Exception e){
			
		}

	}
	public void waitLock(String lockname) {
		IZkDataListener zkDataListener = new IZkDataListener() {
			//临时节点被删除的时候 事件通知所有连接的客户端，客户端唤醒进程
			public void handleDataDeleted(String path) throws Exception {
				
				latch.countDown();//减一
				//lockname.notifyAll();
			}

			public void handleDataChange(String path, Object data) throws Exception {

			}
		};
		// 注册到zkclient进行监听
		zkClient.subscribeDataChanges(ROOT+"/"+lockname, zkDataListener);
		if (zkClient.exists(ROOT+"/"+lockname)) {
			try {
				//lockname.wait();
				latch.await();//堵塞，如果<=0，则往下执行
			} catch (Exception e) {
			}
		}
		// 删除监听
		zkClient.unsubscribeDataChanges(ROOT+"/"+lockname, zkDataListener);
	}
}

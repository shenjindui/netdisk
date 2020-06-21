package com.micro.lock;

public class LockContext {
	private String locktype;
	private String host;
	private Lock lock;
	
	public LockContext(String locktype,String host){
		this.locktype=locktype;
		this.host=host;
	}
	
	//获取锁
	public void getLock(String lockname){
		if("Zookeeper".equals(locktype)){
			lock=LockZookeeper.getInstance(host);
			
		}else if("Redis".equals(locktype)){
			lock=new LockRedis();
	    }else{
	    	throw new RuntimeException("找不到标识locktype=="+locktype);
	    }
		
		lock.getLock(lockname);
	}
	
	//释放锁
	public void unLock(String lockname){
		lock.unLock(lockname);
	}
}

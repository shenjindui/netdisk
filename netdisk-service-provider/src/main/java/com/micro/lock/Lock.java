package com.micro.lock;

public interface Lock {
	//获取锁
	public void getLock(String lockname);
	//释放锁
	public void unLock(String lockname);
}

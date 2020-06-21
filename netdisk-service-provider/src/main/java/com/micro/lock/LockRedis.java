package com.micro.lock;

import org.springframework.stereotype.Component;

@Component("Redis")
public class LockRedis implements Lock{

	@Override
	public void getLock(String lockname) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unLock(String lockname) {
		// TODO Auto-generated method stub
		
	}

}

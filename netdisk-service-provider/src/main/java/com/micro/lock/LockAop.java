package com.micro.lock;

import java.util.concurrent.CountDownLatch;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LockAop {
	public static ThreadLocal<CountDownLatch> tlLatch=new ThreadLocal<>();
	
	@Pointcut("execution(* com.micro.lock.LockZookeeper.getLock(..))")
	private void pointcut(){}
	
	@Before("pointcut()")
	public void beforeDownload(JoinPoint jp){
		//CountDownLatch latch=new CountDownLatch(1);
		//tlLatch.set(latch);
	}
}

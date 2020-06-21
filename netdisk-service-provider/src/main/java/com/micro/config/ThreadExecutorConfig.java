package com.micro.config;


import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author 郑伟业
 * 线程池工具类
 * 
 */
@Configuration
@EnableAsync
public class ThreadExecutorConfig {
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutorLog() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);//配置核心线程数
		executor.setMaxPoolSize(30);//配置最大线程数
		executor.setQueueCapacity(99999);//配置队列大小
		executor.setKeepAliveSeconds(60);
		executor.setThreadNamePrefix("mqExecutor-");//配置线程池中的线程的名称前缀
		
		//对拒绝task的处理策略
		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); 
		
		//执行初始化
		executor.initialize();
		return executor;
	}
}

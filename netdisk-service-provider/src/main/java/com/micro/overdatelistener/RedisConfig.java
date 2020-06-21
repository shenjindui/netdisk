package com.micro.overdatelistener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {
	@Autowired
    private RedisMessageListener messageListener;
	@Autowired 
	private RedisConnectionFactory redisConnectionFactory;
	
    @Bean
    RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListener, new PatternTopic("__keyevent@0__:expired"));
        return container;
    }
}

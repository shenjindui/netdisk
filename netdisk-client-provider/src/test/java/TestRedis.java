import java.util.Collections;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.micro.ClientProviderApp;

@RunWith(SpringJUnit4ClassRunner.class)//junit和spring环境整合
@SpringBootTest(classes=ClientProviderApp.class)
public class TestRedis {
	// Redis获取锁
    private final static String LUA_SCRIPT_LOCK = "return redis.call('SET', KEYS[1], ARGV[1],'EX', ARGV[2],'NX') ";
    private static RedisScript<String> scriptLock = new DefaultRedisScript<String>(LUA_SCRIPT_LOCK, String.class);
 
    //Redis释放锁
    private static final String LUA_SCRIPT_UNLOCK ="if (redis.call('GET', KEYS[1]) == ARGV[1]) then return redis.call('DEL',KEYS[1]) else return 0 end";
    private static RedisScript<Long> scriptUnlock = new DefaultRedisScript<Long>(LUA_SCRIPT_UNLOCK, Long.class);
 
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void test(){
		String key="zwy111";
		String value="123";
		String expireTime="20000";
		String result = redisTemplate.execute(
				scriptLock, 
				redisTemplate.getStringSerializer(), 
				redisTemplate.getStringSerializer(),
                Collections.singletonList(key), 
                value,
                expireTime
				);
		System.out.println("result="+result);
        if (result != null && result.equals("OK")) {
            System.out.println("设置成功.......");
        }else{
        	System.out.println("设置失败.......");
        }
        
        
        /*redisTemplate.execute(
        		scriptUnlock, 
        		redisTemplate.getStringSerializer(), 
        		(RedisSerializer<Long>) redisTemplate.getKeySerializer(),
                Collections.singletonList(key),value
                );*/
	}
}

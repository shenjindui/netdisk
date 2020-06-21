package com.micro.idempotence;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.micro.idempotence.NoRepeatAop.NoRepeatBean;

import lombok.Data;

/**
 * 实现接口幂等性的思路如下
 * 第一步、拦截所有的请求
 * 第二步、在前置增强，去Redis判断是否存在key,key=${userid}-${methodname}
 * 		如果存在，证明上一个执行尚未完成，无法再次执行
 * 		如果不存在，则往Redis新增一条记录，key=${userid}-${methodname}；并且给key设置过期时间（10s钟）
 * 第三步、执行完成（无论是否抛异常），则去redis删除对应记录
 * 第四步、为了提高性能，建议Redis独立，不与业务Redis耦合在一起
 * 
 * 
 * 接口幂等性生效有两种方式
 * * 方案一：通过注解标注，只有加注解的方法才控制幂等性；【推荐】
 * * 方案二：拦截所有的方法，并且加上幂等性；缺点，如果存在并发量很快业务（切块上传），那么就会报错了
 * 
 * @author Administrator
 *
 */
@Aspect
@Component
public class NoRepeatAop {
	@Pointcut("execution(* com.micro.controller.FileController.mergeChunk(..))")
	private void pointcut1(){}
	
	@Pointcut("pointcut1()")
	private void pointcut(){}
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	private final static ThreadLocal<NoRepeatBean> tlRepeat=new ThreadLocal<NoRepeatBean>();
	
	//LUE脚本
	private final static String luaText = "return redis.call('SET', KEYS[1], ARGV[1],'EX', ARGV[2],'NX') ";
	private static RedisScript<String> redisScript = new DefaultRedisScript<String>(luaText, String.class);
		
	@Before("pointcut()")
	public void before(JoinPoint jp) throws NoSuchMethodException, SecurityException{
		Object target=jp.getTarget();
		Signature signature = jp.getSignature();
		String method=signature.getName();
		String targetmethod=signature.getDeclaringTypeName() + "." + method;
		MethodSignature methodSignature = (MethodSignature) signature;
		Class<?>[] types=methodSignature.getParameterTypes();
		Class clazz=target.getClass();
		Method m=clazz.getDeclaredMethod(method, types);
		
		NoRepeat anno=m.getAnnotation(NoRepeat.class);
		if(anno!=null){			
			RequestAttributes ra=RequestContextHolder.getRequestAttributes();
			if(ra!=null){
				ServletRequestAttributes attributes = (ServletRequestAttributes) ra;
				HttpServletRequest request = attributes.getRequest();
				Object userId=request.getParameter("userId");
				if(userId==null){
					userId=request.getAttribute("userId");
					if(userId==null){						
						throw new RuntimeException("NoRepeatAop抛异常,获取userId失败");			
					}
				}
				String key=userId+"-"+targetmethod;
				
				//方式一：Lua脚本没有原子性问题
				String result = stringRedisTemplate.execute(
				        redisScript, 
				        stringRedisTemplate.getStringSerializer(), 
				        stringRedisTemplate.getStringSerializer(),
				        Collections.singletonList(key), 
				        UUID.randomUUID().toString(),
				        20 //20s
				    );
				    
			    if (result != null && result.equals("OK")) {
			    	NoRepeatBean bean=new NoRepeatBean();
					bean.setKey(key);
					bean.setStarttime(System.currentTimeMillis());
					tlRepeat.set(bean);
			    }else{
			        throw new RuntimeException("NoRepeatAop抛异常,无法重复执行");	
			    }
				
				//方式二：有原子性问题
				/*
				String value=stringRedisTemplate.opsForValue().get(key);
				if(StringUtils.isEmpty(value)){
					stringRedisTemplate.opsForValue().set(key, UUID.randomUUID().toString(), 10, TimeUnit.SECONDS);
					NoRepeatBean bean=new NoRepeatBean();
					bean.setKey(key);
					bean.setStarttime(System.currentTimeMillis());
					tlRepeat.set(bean);
					
				}else{				
					throw new RuntimeException("NoRepeatAop抛异常,无法重复执行");			
				}
				*/
				
			}else{
				throw new RuntimeException("NoRepeatAop抛异常,RequestAttributes获取为空NULL");			
			}
		}
	}
	
	@AfterReturning(value = "pointcut()",returning = "result")
    public void afterReturning(JoinPoint jp,Object result) throws NoSuchMethodException, SecurityException{
		Object target=jp.getTarget();
		Signature signature = jp.getSignature();
		String method=signature.getName();
		MethodSignature methodSignature = (MethodSignature) signature;
		Class<?>[] types=methodSignature.getParameterTypes();
		Class clazz=target.getClass();
		Method m=clazz.getDeclaredMethod(method, types);
		
		NoRepeat anno=m.getAnnotation(NoRepeat.class);
		if(anno!=null){			
			NoRepeatBean bean=tlRepeat.get();
			String key=bean.getKey();
			long time=System.currentTimeMillis()-bean.getStarttime();
			if(time<10*1000){//小于10s才执行删除，否则Redis自动过期
				stringRedisTemplate.delete(key);
			}
		}
	}
	
	
	@AfterThrowing(value = "pointcut()",throwing = "ex")
	public void afterThrowing(JoinPoint jp,Exception ex) throws NoSuchMethodException, SecurityException{
		Object target=jp.getTarget();
		Signature signature = jp.getSignature();
		String method=signature.getName();
		MethodSignature methodSignature = (MethodSignature) signature;
		Class<?>[] types=methodSignature.getParameterTypes();
		Class clazz=target.getClass();
		Method m=clazz.getDeclaredMethod(method, types);
		
		NoRepeat anno=m.getAnnotation(NoRepeat.class);
		if(anno!=null){			
			NoRepeatBean bean=tlRepeat.get();
			String key=bean.getKey();
			long time=System.currentTimeMillis()-bean.getStarttime();
			if(time<10*1000){//小于10s才执行删除，否则Redis自动过期
				stringRedisTemplate.delete(key);
			}
		}
	}
	
	@Data
	class NoRepeatBean{
		private long starttime;
		private String key;
	}
}

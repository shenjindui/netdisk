package com.micro.logs.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.rpc.RpcContext;
import com.micro.logs.bean.LogInfo;
import com.micro.logs.bean.LogProperties;
import com.micro.logs.utils.HttpClientUtils;
import com.micro.logs.utils.JsonUtils;
import com.micro.logs.utils.LogContextHolder;

/**
 * 特殊字段的采集
 * 1）projectname
 * 2）host
 * 3）traceid
 * 4）userid
 * 5）username
 * 
 * 有几个难点需要攻克
 * 1）如何通过request.getAttribute("userid")获取的到值呢？？
 * 2）traceId如何能关联Controller工程和Service工程呢？？
 * @author Administrator
 *
 */
@Aspect
@Component
public class CollectionLogAop {
	@Pointcut("execution(* *..controller..*.*(..))")
	private void pointcut1(){}
	
	@Pointcut("execution(* *..service.impl.*.*(..))")
	private void pointcut2(){}
	
	@Pointcut("pointcut1() || pointcut2()")
	private void pointcut(){}
	
	@Autowired
	private LogProperties logProperties;
	
	private DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//前置aop
	@Before("pointcut()")
	public void before(JoinPoint jp){
		if("on".equals(logProperties.getState())){
			try{
				Object target=jp.getTarget();
				Signature signature = jp.getSignature();
				String method=signature.getName();
				
				//目标对象
				String targetmethod=signature.getDeclaringTypeName() + "." + method;
				//目标参数
				MethodSignature methodSignature = (MethodSignature) signature;
				Class<?>[] types=methodSignature.getParameterTypes();
				String[] names = methodSignature.getParameterNames();
				Object[] values = jp.getArgs();
				
				Class clazz=target.getClass();
				Method m=clazz.getDeclaredMethod(method, types);//Method m=clazz.getMethod(method, types);
				
				//Logs logs=m.getAnnotation(Logs.class);
				LogInfo bean=LogContextHolder.get();
				if(bean==null){
					bean=new LogInfo();
				}
				//基本信息
				bean.setProjectname(logProperties.getProjectname());
				bean.setProjectdesc(logProperties.getProjectdesc());
				bean.setTargetmethod(targetmethod);
				bean.setStarttime(df.format(new Date()));
				
				RequestAttributes ra=RequestContextHolder.getRequestAttributes();
				if(ra!=null){
					ServletRequestAttributes attributes = (ServletRequestAttributes) ra;
					HttpServletRequest request = attributes.getRequest();
					
					//IP地址
					String requestip=request.getRemoteAddr();//controller
					bean.setRequestip(requestip);
					
					//用户信息【针对Controller工程，Service工程无法获取userid和username】
					bean.setUserid(request.getAttribute("userid")==null?"":request.getAttribute("userid").toString());
					bean.setUsername(request.getAttribute("username")==null?"":request.getAttribute("username").toString());
				}
				//traceid
				String traceid=RpcContext.getContext().getAttachment("traceid");
				if(!StringUtils.isEmpty(traceid)){
					if(StringUtils.isEmpty(bean.getTraceid())){					
						bean.setTraceid(traceid);
					}
				}
				//requestip
				String requestip=RpcContext.getContext().getAttachment("requestip");
				if(!StringUtils.isEmpty(requestip)){
					if(StringUtils.isEmpty(bean.getRequestip())){					
						bean.setRequestip(requestip);
					}
				}
				
				//userid
				String userid=RpcContext.getContext().getAttachment("userid");
				if(!StringUtils.isEmpty(userid)){
					if(StringUtils.isEmpty(bean.getUserid())){					
						bean.setUserid(userid);
					}
				}
				//username
				String username=RpcContext.getContext().getAttachment("username");
				if(!StringUtils.isEmpty(username)){
					if(StringUtils.isEmpty(bean.getUsername())){					
						bean.setUsername(username);
					}
				}
				
				//请求参数
				Map<String,Object> map=new HashMap<String,Object>();
				for(int i=0;i<names.length;i++){
					Class<?> type=types[i];
					String name=names[i];
					Object value=values[i];
					String key=type.getSimpleName()+"."+name;
					if(type.getSimpleName().equals("HttpServletRequest")){
						value="HttpServletRequest对象";
					}
					if(type.getSimpleName().equals("HttpServletResponse")){
						value="HttpServletResponse对象";				
					}
					if(type.getSimpleName().equals("MultipartFile")){
						value="MultipartFile对象";
					}
					if(type.getSimpleName().equals("byte[]")){
						value="字节数组";
					}
					map.put(key,value);
				}
				bean.setTargetparams(JsonUtils.objectToJson(map));
				
				//保存线程变量
				LogContextHolder.set(bean);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@AfterReturning(value = "pointcut()",returning = "result")
    public void afterReturning(JoinPoint jp,Object result){
		if("on".equals(logProperties.getState())){			
			LogInfo bean=LogContextHolder.get();
			if(bean!=null){
				bean.setEndtime(df.format(new Date()));//设置结束时间
				bean.setComsumetime(getComsumetime(bean.getStarttime(), bean.getEndtime()));//计算耗时
				bean.setExecuteresult("执行成功");
				
				if(result!=null){				
					if(result.getClass().getSimpleName().equals("byte[]")){
						Map<String,Object> map=new HashMap<String,Object>();
						map.put("byte","字节数组");
						bean.setReturnresult(JsonUtils.objectToJson(map));
					}else{				
						bean.setReturnresult(JsonUtils.objectToJson(result));//返回结果
					}
				}
				
				//【针对controller工程】webuploader上传时，无法在拦截器获取token，只能在代码里面获取
				if(StringUtils.isEmpty(bean.getUserid())){
					RequestAttributes ra=RequestContextHolder.getRequestAttributes();
					if(ra!=null){
						ServletRequestAttributes attributes = (ServletRequestAttributes) ra;
						HttpServletRequest request = attributes.getRequest();					
						bean.setUserid(request.getAttribute("userid")==null?"":request.getAttribute("userid").toString());
						bean.setUsername(request.getAttribute("username")==null?"":request.getAttribute("username").toString());
					}
				}
				
				LogContextHolder.clear();//清空
				saveLogs(bean);
			}
		}
    }

    @AfterThrowing(value = "pointcut()",throwing = "ex")
    public void afterThrowing(JoinPoint jp,Exception ex){
    	if("on".equals(logProperties.getState())){    		
    		LogInfo bean=LogContextHolder.get();
    		if(bean!=null){
    			bean.setEndtime(df.format(new Date()));//设置结束时间
    			bean.setComsumetime(getComsumetime(bean.getStarttime(), bean.getEndtime()));//计算耗时
    			bean.setExecuteresult(ex.getMessage());//执行结果
    			bean.setReturnresult("");//返回结果
    			
    			//【针对controller工程】webuploader上传时，无法在拦截器获取token，只能在代码里面获取
    			if(StringUtils.isEmpty(bean.getUserid())){
    				RequestAttributes ra=RequestContextHolder.getRequestAttributes();
    				if(ra!=null){
    					ServletRequestAttributes attributes = (ServletRequestAttributes) ra;
    					HttpServletRequest request = attributes.getRequest();					
    					bean.setUserid(request.getAttribute("userid")==null?"":request.getAttribute("userid").toString());
    					bean.setUsername(request.getAttribute("username")==null?"":request.getAttribute("username").toString());
    				}
    			}
    			
    			LogContextHolder.clear();//清空
    			saveLogs(bean);
    		}
    	}
    }
    
    private ExecutorService es=Executors.newCachedThreadPool();
    public void saveLogs(LogInfo bean){
    	if("on".equals(logProperties.getState())){    		
    		es.execute(new Runnable() {
    			@Override
    			public void run() {
    				Map<String,String> params=bean2Map(bean);
    				
    				String host=logProperties.getHost();
    				if(!host.startsWith("http://")){
    					host="http://"+host;
    				}
    				String path=host+"/netdisk-log-provider/log/collect/collectLog";
    				HttpClientUtils.doPost(path, params);
    				
    				//为了提高性能，不用每次都发送日志，可以做以下优化
    				//方案一：每个5s批量发送一次日志
    				//方案二：攒够50条日志，批量发送一次
    			}
    		});
    	}
		
	}
    
    public static Map<String, String> bean2Map(Object obj) {
        Map<String, String> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                
                map.put(field.getName(), field.get(obj)==null?"":field.get(obj).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    public long getComsumetime(String starttime,String endtime){
    	try {
			return df.parse(endtime).getTime()-df.parse(starttime).getTime();
		} catch (ParseException e) {
			return 0l;
		}
    }
}

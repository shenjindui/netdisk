package com.micro.logs.dubbofilter;

import java.util.UUID;

import org.springframework.util.StringUtils;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.micro.logs.bean.LogInfo;
import com.micro.logs.utils.LogContextHolder;

/**
 * 测试
 * 1）释放可以抽取公共（META-INF/dubbo配置在公共工程）【可以】
 * 
 * 2）是否需要配置filter【需要】
 * 		方案一：针对具体
 * 			@Reference(check=false,filter={"logFilter"})
			private FileService fileService;
		方案二：针对全局
			@Activate(group = {Constants.PROVIDER, Constants.CONSUMER},order = -2000)
 * 		
 * 3）整个执行顺序是什么样的？
 * 		controller.aop开始->controller方法->filter->controller.aop结束======>filter->service.aop开始->service方法->service.aop结束
 * 
 * @author Administrator
 *
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER},order = -2000)
public class GlobalTraceFilter implements Filter{

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		String traceId = invocation.getAttachment("traceId");
        if(StringUtils.isEmpty(traceId)) {
        	traceId=UUID.randomUUID().toString();
            RpcContext.getContext().setAttachment("traceId",traceId);
        }else { 
        	RpcContext.getContext().setAttachment("traceId",traceId);
        }
        
        //生成traceId
        LogInfo bean=LogContextHolder.get();
		if(bean!=null){	//接口消费方信息补全（controller/service）	
			bean.setTraceid(traceId);//traceid
			LogContextHolder.clear();//清空
			LogContextHolder.set(bean);//赋值
			
			RpcContext.getContext().setAttachment("userid",bean.getUserid());
			RpcContext.getContext().setAttachment("username",bean.getUsername());
			
		}else{//接口提供方信息补全（service）
			bean=new LogInfo();
			//traceid
			bean.setTraceid(traceId);
			//ip地址
			if(StringUtils.isEmpty(bean.getRequestip())){
				String requestip=RpcContext.getContext().getRemoteAddressString();
				bean.setRequestip(requestip);
			}
			//userid
			bean.setUserid(RpcContext.getContext().getAttachment("userid"));
			//usrname
			bean.setUsername(RpcContext.getContext().getAttachment("username"));
			
			//赋值
			LogContextHolder.set(bean);
		}
		
        return invoker.invoke(invocation);
	}

}

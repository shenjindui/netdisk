package com.micro.netdisk.javasdk.balance;

import java.util.List;

/**
 * 轮询算法
 * @author Administrator
 *
 */
public class RoundbinLoadBalance extends AbstractLoadBalance{
	private static int pos=0;//当前位置
	
    @Override
    protected String doSelect(List<HostBean> lists){
	    
    	String result="";
    	synchronized (RoundbinLoadBalance.class) {
			if(pos>=lists.size()){
				pos=0;
			}
			result=lists.get(pos).getIp()+":"+lists.get(pos).getPort();
			pos++;
		}
        
        return result;
    }
}

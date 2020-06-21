package com.micro.netdisk.javasdk.balance;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 负载算法抽象类
 * 目的：把通过功能给封装起来
 * @author Administrator
 *
 */
public abstract class AbstractLoadBalance implements ILoadBalance{

	@Override
	public String select(List<HostBean> lists) {
		if(lists==null||lists.size()==0){
            return null;
        }
        if(lists.size()==1){
        	return lists.get(0).getIp()+":"+lists.get(0).getPort();
        }
        return doSelect(lists);
	}
	
	protected abstract String doSelect(List<HostBean> lists);
}

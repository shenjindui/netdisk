package com.micro.netdisk.javasdk.balance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.micro.netdisk.javasdk.factory.BalanceMode;
import com.micro.netdisk.javasdk.factory.FileFactory;

public class LoadBalanceContext{
	private volatile static ILoadBalance lb=null;
	public static String select(Set<HostBean> lists){
		if(lb==null){
			synchronized (LoadBalanceContext.class) {				
				if(lb==null){					
					String balance=FileFactory.balance;
					if(balance.equals(BalanceMode.BALANCE_RANDOM)){
						lb=new RandomLoadBalance();
					}else if(balance.equals(BalanceMode.BALANCE_RANDOMANDWEIGHT)){
						lb=new RandomWeightLoadBalance();
					}else if(balance.equals(BalanceMode.BALANCE_ROUNDBIN)){
						lb=new RoundbinLoadBalance();
					}else if(balance.equals(BalanceMode.BALANCE_ROUNDBINWEIGHT)){
						lb=new RoundbinWeightLoadBalance();
					}
				}
			}
		}
		if(lists.isEmpty()){
			return "";
		}
		List<HostBean> beans=new ArrayList<>();
		Iterator<HostBean> it = lists.iterator();
		if(it.hasNext()){
			beans.add(it.next());
		}
		return lb.select(beans);
	}
}

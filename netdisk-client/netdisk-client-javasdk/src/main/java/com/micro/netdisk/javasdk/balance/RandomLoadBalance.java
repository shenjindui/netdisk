package com.micro.netdisk.javasdk.balance;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 随机算法
 * @author Administrator
 *
 */
public class RandomLoadBalance extends AbstractLoadBalance{

	@Override
	protected String doSelect(List<HostBean> lists){
		int len=lists.size();
        Random random=new Random();
        int index=random.nextInt(len);
        
        return lists.get(index).getIp()+":"+lists.get(index).getPort();
	}

}

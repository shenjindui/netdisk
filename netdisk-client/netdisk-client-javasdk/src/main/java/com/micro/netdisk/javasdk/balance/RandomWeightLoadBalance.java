package com.micro.netdisk.javasdk.balance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomWeightLoadBalance extends AbstractLoadBalance{

	@Override
	protected String doSelect(List<HostBean> lists) {
		ArrayList<String> arrs = new ArrayList<String>();
		
		//根据权重来生成比重的数据放入List集合
		for(HostBean bean:lists){
			int weight=bean.getWeight();
			String host=bean.getIp()+":"+bean.getPort();
			for(int i=0;i<weight;i++){
				arrs.add(host);
			}
		}
		//开始随机
		int len=arrs.size();
        Random random=new Random();
        int index=random.nextInt(len);
        
        return arrs.get(index);
	}

}

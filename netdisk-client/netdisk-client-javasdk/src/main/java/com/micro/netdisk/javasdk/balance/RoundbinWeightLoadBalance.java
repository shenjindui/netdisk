package com.micro.netdisk.javasdk.balance;

import java.util.ArrayList;
import java.util.List;

public class RoundbinWeightLoadBalance extends AbstractLoadBalance{
	private static int pos=0;//当前位置
	
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
		//开始轮询
		String result="";
    	synchronized (RoundbinLoadBalance.class) {
			if(pos>=lists.size()){
				pos=0;
			}
			result=arrs.get(pos);
			pos++;
		}
        
        return result;
	}

}

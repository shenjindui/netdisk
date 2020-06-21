package com.micro.netdisk.javasdk.balance;

import java.util.List;
import java.util.Set;

/**
 * 负载算法接口
 * @author Administrator
 *
 */
public interface ILoadBalance {
	public String select(List<HostBean> lists);
}

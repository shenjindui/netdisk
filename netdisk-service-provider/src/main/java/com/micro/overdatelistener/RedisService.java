package com.micro.overdatelistener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.micro.chain.core.Bootstrap;
import com.micro.chain.core.HandlerInitializer;
import com.micro.chain.core.Pipeline;
import com.micro.chain.handler.OverDueGetDataHandler;
import com.micro.chain.handler.OverDueRubbishDelHandler;
import com.micro.chain.handler.OverDueRubbishNoticeHandler;
import com.micro.chain.handler.OverDueRubbishPushHandler;
import com.micro.chain.handler.OverDueShareGetDataHandler;
import com.micro.chain.handler.OverDueShareNoticeHandler;
import com.micro.chain.handler.OverDueSharePushHandler;
import com.micro.chain.handler.OverDueShareUpdateHandler;
import com.micro.chain.param.OverDueRubbishRequest;
import com.micro.chain.param.OverDueShareRequest;
import com.micro.common.Contanst;
import com.micro.db.dao.DiskMd5ChunkDao;
import com.micro.store.context.StoreContext;
import com.micro.store.service.StoreService;
import com.micro.utils.SpringContentUtils;

@Component
public class RedisService {
	@Autowired
	private StoreContext storeContext;
	@Autowired
	private DiskMd5ChunkDao diskMd5ChunkDao;
	@Autowired
	private SpringContentUtils springContentUtils;
	
	//注意细节
	//1、集群模式，每个工程都会执行，防止重复执行
	//1.1、加分布式锁
	//1.2、需要判断map是否为空，需要判断status是否已经更改了
	public void overdueShare(String id) {
		OverDueShareRequest request=new OverDueShareRequest();
		request.setId(id);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.根据ID查询实体
				pipeline.addLast(springContentUtils.getHandler(OverDueShareGetDataHandler.class));
				//2.更改分享状态				
				pipeline.addLast(springContentUtils.getHandler(OverDueShareUpdateHandler.class));
				//3.保存通知
				pipeline.addLast(springContentUtils.getHandler(OverDueShareNoticeHandler.class));
				//4.只推送本人，因为只有私密链接才有过期的，好友是没有过期的
				pipeline.addLast(springContentUtils.getHandler(OverDueSharePushHandler.class));
			}
		});
		bootstrap.execute();
	}
	
	//注意细节
	//1、防止重复删除，判断map是否为空
	public void overdueRubbish(String id) {
		OverDueRubbishRequest request=new OverDueRubbishRequest();
		request.setId(id);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.根据ID查询实体
				pipeline.addLast(springContentUtils.getHandler(OverDueGetDataHandler.class));
				//2.递归删除
				pipeline.addLast(springContentUtils.getHandler(OverDueRubbishDelHandler.class));
				//3.保存通知
				pipeline.addLast(springContentUtils.getHandler(OverDueRubbishNoticeHandler.class));
				//4.推送给删除的人
				pipeline.addLast(springContentUtils.getHandler(OverDueRubbishPushHandler.class));
			}
		});
		bootstrap.execute();
	}
	
	@Deprecated
	public void deleteTemps(String key) {
		String[] arrs=null;//key.split(Contanst.SEPARATOR);
		String keyPrefix=arrs[0];
		String chunk=arrs[1];
		String storepath=arrs[2];
		
		//问题：为什么不在mergeFile成功的时候删除Redis和切块记录呢？
			//1.代码耦合在一起了
			//2.循环删除切块，造成耗时比较长，给用户的响应时间比较慢
			//3.做容错处理，增加上传代码的复杂度，比如：删除Redis失败、删除切块失败之后，需要通过某种几种来重复删除
		
		//问题：mergeFile，md5查询不存在的情况保存成功删除Redis，md5查询存在的时候不删除Redis
			//1.这种情况确实可以保护有效切块不会被Redis过期监听时删除掉
			//2.万一Redis删除失败了，此时Redis监听还是会把有效的切块给删除了
		
		//如何防止频繁的查询disk_md5_chunk造成的MySQL压力呢？
			//1.给storepath增加索引，提供查询速度
			//2.后期把disk_md5_chunk表同步到Redis
		Integer count=diskMd5ChunkDao.findCountByStorepath(storepath);
		if(count==null||count==0){
			storeContext.delete(storepath);			
		}
	}
}

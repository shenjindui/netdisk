package com.micro.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.micro.chain.core.Bootstrap;
import com.micro.chain.core.HandlerInitializer;
import com.micro.chain.core.Pipeline;
import com.micro.chain.handler.RubbishClearDelHandler;
import com.micro.chain.handler.RubbishClearGetDataHandler;
import com.micro.chain.handler.RubbishClearRedisHandler;
import com.micro.chain.handler.RubbishClearValidateHandler;
import com.micro.chain.handler.RubbishRecoverCapacityEnoughHandler;
import com.micro.chain.handler.RubbishRecoverCapacityUpdateHandler;
import com.micro.chain.handler.RubbishRecoverGetDataHandler;
import com.micro.chain.handler.RubbishRecoverRedisHandler;
import com.micro.chain.handler.RubbishRecoverSolrHandler;
import com.micro.chain.handler.RubbishRecoverValidateHandler;
import com.micro.chain.handler.RubbishReoverInsertHandler;
import com.micro.chain.param.RubbishClearRequest;
import com.micro.chain.param.RubbishRecoverRequest;
import com.micro.db.jdbc.DiskFileDelJdbc;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.RubbishBean;
import com.micro.disk.service.RubbishService;
import com.micro.utils.SpringContentUtils;

@Service(interfaceClass=RubbishService.class)
@Component
@Transactional
public class RubbishServiceImpl implements RubbishService{
	@Autowired
	private DiskFileDelJdbc diskFileDelJdbc;
	@Autowired
	private SpringContentUtils springContentUtils;
	
	@Override
	public PageInfo<RubbishBean> findPageList(Integer page, Integer limit, String userid) {
		return diskFileDelJdbc.findPageList(page, limit, userid);
	}

	@Override
	public void delete(List<String> ids,String userid) {
		RubbishClearRequest request=new RubbishClearRequest();
		request.setIds(ids);
		request.setUserid(userid);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(RubbishClearValidateHandler.class));
				//2.递归获取数据
				pipeline.addLast(springContentUtils.getHandler(RubbishClearGetDataHandler.class));
				//3.删除
				pipeline.addLast(springContentUtils.getHandler(RubbishClearDelHandler.class));
				//4.删除Redis对应的key
				pipeline.addLast(springContentUtils.getHandler(RubbishClearRedisHandler.class));
			}
		});
		bootstrap.execute();
	}
	
	@Override
	public void recover(String folderid,List<String> ids,String userid,String username) {
		RubbishRecoverRequest request=new RubbishRecoverRequest();
		request.setFolderid(folderid);
		request.setIds(ids);
		request.setUserid(userid);
		request.setUsername(username);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(RubbishRecoverValidateHandler.class));
				//2.递归获取数据
				pipeline.addLast(springContentUtils.getHandler(RubbishRecoverGetDataHandler.class));
				//3.判断容量是否足够
				pipeline.addLast(springContentUtils.getHandler(RubbishRecoverCapacityEnoughHandler.class));
				//4.开始还原
				pipeline.addLast(springContentUtils.getHandler(RubbishReoverInsertHandler.class));
				//5.新增Solr
				pipeline.addLast(springContentUtils.getHandler(RubbishRecoverSolrHandler.class));
				//6.删除Redis
				pipeline.addLast(springContentUtils.getHandler(RubbishRecoverRedisHandler.class));
				//7.更新容量、推送容量到前端
				pipeline.addLast(springContentUtils.getHandler(RubbishRecoverCapacityUpdateHandler.class));
				//8.最近操作
			}
		});
		bootstrap.execute();
	}
	
}

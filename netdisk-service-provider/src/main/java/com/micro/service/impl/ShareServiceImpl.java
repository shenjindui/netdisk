package com.micro.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.micro.chain.core.Bootstrap;
import com.micro.chain.core.HandlerInitializer;
import com.micro.chain.core.Pipeline;
import com.micro.chain.handler.ShareCancelFriendHandler;
import com.micro.chain.handler.ShareCancelSecretHandler;
import com.micro.chain.handler.ShareCancelUpdateHandler;
import com.micro.chain.handler.ShareCancelValidateHandler;
import com.micro.chain.handler.ShareFriendsFromAlbumHandler;
import com.micro.chain.handler.ShareFriendsFromFileHandler;
import com.micro.chain.handler.ShareFriendsNoticeHandler;
import com.micro.chain.handler.ShareFriendsSaveFriendsHandler;
import com.micro.chain.handler.ShareFriendsSaveHandler;
import com.micro.chain.handler.ShareFriendsValidateHandler;
import com.micro.chain.handler.ShareSecretDetailFromAlbumHandler;
import com.micro.chain.handler.ShareSecretDetailFromFileHandler;
import com.micro.chain.handler.ShareSecretRedisHandler;
import com.micro.chain.handler.ShareSecretSaveHandler;
import com.micro.chain.handler.ShareSecretValidateHandler;
import com.micro.chain.param.ShareCancelRequest;
import com.micro.chain.param.ShareFriendsRequest;
import com.micro.chain.param.ShareSecretRequest;
import com.micro.chain.param.ShareSecretResponse;
import com.micro.common.Contanst;
import com.micro.common.DateUtils;
import com.micro.common.ValidateUtils;
import com.micro.db.dao.DiskShareDao;
import com.micro.db.jdbc.DiskShareFileJdbc;
import com.micro.db.jdbc.DiskShareFriendsJdbc;
import com.micro.db.jdbc.DiskShareJdbc;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.ShareBean;
import com.micro.disk.bean.ShareFileBean;
import com.micro.disk.bean.ShareFriendsBean;
import com.micro.disk.bean.ShareSecretResult;
import com.micro.disk.service.ShareService;
import com.micro.model.DiskShare;
import com.micro.utils.SpringContentUtils;

@Service(interfaceClass=ShareService.class)
@Component
@Transactional
public class ShareServiceImpl implements ShareService{
	@Autowired
	private DiskShareDao diskShareDao;
	@Autowired
	private DiskShareJdbc diskShareJdbc;
	@Autowired
	private DiskShareFileJdbc diskShareFileJdbc;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private DiskShareFriendsJdbc diskShareFriendsJdbc;
	@Autowired
	private SpringContentUtils springContentUtils;
	
	@Override
	public ShareSecretResult shareSecret(List<String> ids,String title, String userid, String username, Integer sharetype,Integer effect,Integer type) {
		ShareSecretRequest request=new ShareSecretRequest();
		request.setIds(ids);
		request.setTitle(title);
		request.setUserid(userid);
		request.setUsername(username);
		request.setSharetype(sharetype);
		request.setEffect(effect);
		request.setType(type);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,new ShareSecretResponse()) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(ShareSecretValidateHandler.class));
				//2.保存disk_share
				pipeline.addLast(springContentUtils.getHandler(ShareSecretSaveHandler.class));
				//3.保存disk_share_file（相册分享）
				pipeline.addLast(springContentUtils.getHandler(ShareSecretDetailFromAlbumHandler.class));
				//4.保存disk_share_file（文件分享）
				pipeline.addLast(springContentUtils.getHandler(ShareSecretDetailFromFileHandler.class));
				//5.添加Redis监听过期
				pipeline.addLast(springContentUtils.getHandler(ShareSecretRedisHandler.class));
				//6.最近操作
			}
		});
		ShareSecretResponse res=(ShareSecretResponse) bootstrap.execute();
		
		ShareSecretResult result=new ShareSecretResult();
		result.setUrl(res.getUrl());
		result.setCode(res.getCode());
		return result;
	}
	
	@Override
	public void shareFriends(List<String> ids,List<ShareFriendsBean> friends,String title,String userid,String username,Integer type) {
		ShareFriendsRequest request=new ShareFriendsRequest();
		request.setIds(ids);
		request.setFriends(friends);
		request.setTitle(title);
		request.setUserid(userid);
		request.setUsername(username);
		request.setType(type);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(ShareFriendsValidateHandler.class));
				//2.保存disk_share
				pipeline.addLast(springContentUtils.getHandler(ShareFriendsSaveHandler.class));
				//3.保存disk_share_friends
				pipeline.addLast(springContentUtils.getHandler(ShareFriendsSaveFriendsHandler.class));
				//4.保存disk_share_file（相册分享）
				pipeline.addLast(springContentUtils.getHandler(ShareFriendsFromAlbumHandler.class));
				//5.保存disk_share_file（文件分享）
				pipeline.addLast(springContentUtils.getHandler(ShareFriendsFromFileHandler.class));
				//6.保存通知、推送好友
				pipeline.addLast(springContentUtils.getHandler(ShareFriendsNoticeHandler.class));
				//7.最近操作
			}
		});
		bootstrap.execute();
	}
	
	@Override
	public ShareBean findShareInfo(String id) {
		ValidateUtils.validate(id, "分享ID");
		id=id.toLowerCase();
		DiskShare share=diskShareDao.findOne(id);
		if(share==null){
			throw new RuntimeException("找不到分享记录");
		}
		ShareBean sb=new ShareBean();
		sb.setId(share.getId());
		sb.setTitle(share.getTitle());
		sb.setShareuser(share.getShareusername());
		sb.setSharetime(DateUtils.formatDate(share.getSharetime(),"yyyy-MM-dd HH:mm:ss"));
		
		String effectname="";
		if(share.getEffect()==0){
			effectname="永久有效";
		}else {
			effectname=share.getEffect()+"天有效";
		}
		sb.setEffectname(effectname);
		sb.setStatus(share.getStatus());
		sb.setSharetype(share.getSharetype());
		return sb;
	}
	@Override
	public String validateCode(String id, String code) {
		ValidateUtils.validate(id, "分享ID");
		ValidateUtils.validate(code, "提取码");
		id=id.toLowerCase();
		DiskShare share=diskShareDao.findOne(id);
		if(share==null){
			throw new RuntimeException("该链接地址无效");
		}
		if(share.getType()!=0){
			throw new RuntimeException("不是私密链接分享");
		}
		if(share.getSharetype()!=0){
			throw new RuntimeException("不是有码提取");
		}
		if(!share.getCode().equals(code)){
			throw new RuntimeException("提取码不正确");
		}
		if(share.getEffect()!=0){
			if(share.getEndtime().before(new Date())){
				throw new RuntimeException("该链接已经失效");
			}
		}
		if(share.getStatus()==1){
			throw new RuntimeException("该链接已经失效");
		}
		if(share.getStatus()==2){
			throw new RuntimeException("该链接已经取消分享");
		}
		
		String token=UUID.randomUUID().toString();
		if(share.getType()==0&&share.getSharetype()==0){
			//私密链接且有码提取，则生成token
			stringRedisTemplate.opsForValue().set(Contanst.PREFIX_SHARE_CODE+token, token, 10, TimeUnit.MINUTES);
			return token;
		}
		return "";
	}
	@Override
	public List<ShareFileBean> findShareFileListFromSecret(String id,String pid,String token) {
		ValidateUtils.validate(id, "分享ID");
		if(StringUtils.isEmpty(pid)){
			pid="0";
		}
		id=id.toLowerCase();
		DiskShare share=diskShareDao.findOne(id);
		if(share==null){
			throw new RuntimeException("找不到分享记录");
		}
		
		//私密链接且有码提取，需要校验token
		if(share.getType()==0&&share.getSharetype()==0){
			String value=stringRedisTemplate.opsForValue().get(Contanst.PREFIX_SHARE_CODE+token);
			if(StringUtils.isEmpty(value)){
				throw new RuntimeException("您尚未认证提取码或提取码已经失效,请重新认证!");
			}else{				
				stringRedisTemplate.expire(Contanst.PREFIX_SHARE_CODE+token, 10, TimeUnit.MINUTES);
			}
		}
		//验证是否已否过期
		if(share.getStatus()==1){
			throw new RuntimeException("该分享已失效");
		}
		//验证是否是否取消
		if(share.getStatus()==2){
			throw new RuntimeException("该分享已取消");
		}
		
		//查询列表
		return diskShareFileJdbc.findListChild(id, pid);
	}
	@Override
	public List<ShareFileBean> findShareFileListFromFriends(String id,String pid) {
		ValidateUtils.validate(id, "分享ID");
		if(StringUtils.isEmpty(pid)){
			pid="0";
		}
		DiskShare share=diskShareDao.findOne(id);
		if(share==null){
			throw new RuntimeException("找不到分享记录");
		}
		if(share.getStatus()==1){
			throw new RuntimeException("该分享已失效");
		}
		if(share.getStatus()==2){
			throw new RuntimeException("该分享已取消");
		}
		
		//查询列表
		return diskShareFileJdbc.findListChild(id, pid);
	}
	@Override
	public List<ShareFileBean> findShareFileListFromSelf(String id,String pid) {
		ValidateUtils.validate(id, "分享ID");
		if(StringUtils.isEmpty(pid)){
			pid="0";
		}
		return diskShareFileJdbc.findListChild(id, pid);
	}
	
	@Override
	public void validateShareIsEffect(String shareid) {
		ValidateUtils.validate(shareid, "分享ID");
		DiskShare share=diskShareDao.findOne(shareid);
		if(share==null){
			throw new RuntimeException("分享ID不存在");
		}
		if(share.getType()==0){//私密链接			
			if(share.getEffect()!=0){
				if(share.getEndtime().before(new Date())){
					throw new RuntimeException("该分享已经失效");
				}
			}
			if(share.getStatus()==1){
				throw new RuntimeException("该分享已经失效");
			}
			if(share.getStatus()==2){
				throw new RuntimeException("该分享已经被取消了");
			}
		}
	}
	@Override
	public PageInfo<ShareBean> findMyShare(Integer page, Integer limit, String userid,Integer type,Integer status) {
		return diskShareJdbc.findMyShare(page, limit, userid,type,status);
	}
	
	@Override
	public PageInfo<ShareBean> findFriendsShare(Integer page, Integer limit, String userid, Integer status) {
		return diskShareJdbc.findFriendsShare(page, limit, userid, status);
	}
	
	@Override
	public void cancelShare(List<String> ids) {
		ShareCancelRequest request=new ShareCancelRequest();
		request.setIds(ids);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(ShareCancelValidateHandler.class));
				//2.更改分享状态
				pipeline.addLast(springContentUtils.getHandler(ShareCancelUpdateHandler.class));
				//3.私密链接，并且设置有效期的，删除对应的Redis监听
				pipeline.addLast(springContentUtils.getHandler(ShareCancelSecretHandler.class));
				//4.好友分享，给好友推送信息
				pipeline.addLast(springContentUtils.getHandler(ShareCancelFriendHandler.class));
			}
		});
		bootstrap.execute();
	}

	@Override
	public List<ShareFriendsBean> findFriends(String shareid) {
		
		return diskShareFriendsJdbc.findFriends(shareid);
	}

}

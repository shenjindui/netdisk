package com.micro.disk.service;

import java.util.List;
import java.util.Map;

import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.ShareBean;
import com.micro.disk.bean.ShareFileBean;
import com.micro.disk.bean.ShareFriendsBean;
import com.micro.disk.bean.ShareSecretResult;

public interface ShareService {
	/**
	 * 私密链接分享
	 * @param ids
	 * @param title
	 * @param userid
	 * @param username
	 * @param sharetype
	 * @param effect
	 * @param type 0普通分享，1相册分享
	 * @return
	 */
	public ShareSecretResult shareSecret(List<String> ids,String title,String userid,String username,Integer sharetype,Integer effect,Integer type);
	
	/**
	 * 好友分享
	 * @param ids
	 * @param friends
	 * @param title
	 * @param userid
	 * @param username
	 * @param type 0普通分享，1相册分享
	 */
	public void shareFriends(List<String> ids,List<ShareFriendsBean> friends,String title,String userid,String username,Integer type);
	
	/**
	 * 分享基本信息
	 * @param id
	 * @return
	 */
	public ShareBean findShareInfo(String id);
	
	/**
	 * 校验code
	 * @param id
	 * @param code
	 */
	public String validateCode(String id,String code);
	
	/**
	 * 分享文件列表（私密链接）
	 * @param id
	 * @param pid
	 * @param token
	 * @return
	 */
	public List<ShareFileBean> findShareFileListFromSecret(String id,String pid,String token);
	/**
	 * 分享文件列表（好友）
	 * @param id
	 * @param pid
	 * @return
	 */
	public List<ShareFileBean> findShareFileListFromFriends(String id,String pid);
	/**
	 * 分享文件列表（我的分享）
	 * @param id
	 * @param pid
	 * @return
	 */
	public List<ShareFileBean> findShareFileListFromSelf(String id,String pid);
	
	/**
	 * 验证分享是否有效
	 * @param shareid
	 */
	public void validateShareIsEffect(String shareid);
	/**
	 * 我的分享列表
	 * @param page
	 * @param limit
	 * @param userid
	 * @param type 0私密链接分享，1好友分享
	 * @param status 0正常，1已失效，2已过期
	 * @return
	 */
	public PageInfo<ShareBean> findMyShare(Integer page,Integer limit,String userid,Integer type,Integer status);
	/**
	 * 好友的分享列表
	 * @param page
	 * @param limit
	 * @param userid
	 * @param status 0正常，1已失效，2已过期
	 * @return
	 */
	public PageInfo<ShareBean> findFriendsShare(Integer page,Integer limit,String userid,Integer status);
	
	/**
	 * 取消分享
	 * @param id
	 */
	public void cancelShare(List<String> ids);
	
	/**
	 * 查询接受的好友
	 * @param shareid
	 * @return
	 */
	public List<ShareFriendsBean> findFriends(String shareid);
}

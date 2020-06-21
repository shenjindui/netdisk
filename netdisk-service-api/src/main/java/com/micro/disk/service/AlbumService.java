package com.micro.disk.service;

import java.util.List;

import com.micro.disk.bean.AlbumBean;
import com.micro.disk.bean.FileBean;
import com.micro.disk.bean.PageInfo;

public interface AlbumService {
	/**
	 * 查询相册（分页）
	 * @param page
	 * @param limit
	 * @param userid
	 * @param albumname
	 * @return
	 */
	public PageInfo<AlbumBean> findPageList(Integer page,Integer limit,String userid,String albumname);
	
	/**
	 * 查询相册
	 * @param userid
	 * @return
	 */
	public List<AlbumBean> findList(String userid);
	
	/**
	 * 查询单条记录
	 * @param albumid 相册id
	 * @return
	 */
	public AlbumBean findOne(String albumid);
	
	/**
	 * 新增相册
	 * @param albumname
	 * @param albumdesc
	 * @param userid
	 * @param username
	 */
	public void addAlbum(String albumname,String albumdesc,String userid,String username);
	/**
	 * 修改相册
	 * @param albumid
	 * @param albumname
	 * @param albumdesc
	 */
	public void updateAlbum(String albumid,String albumname,String albumdesc);
	/**
	 * 删除相册
	 * @param albumid
	 */
	public void deleteAlbum(String albumid);
	/**
	 * 删除相册（并删除下面的图片）
	 * @param userid
	 * @param username
	 * @param albumid
	 */
	public void deleteAlbumCascade(String userid,String username,String albumid);
	
	/**
	 * 查询某个相册下面的图片分页列表
	 * @param page
	 * @param limit
	 * @param albumid
	 * @return
	 */
	public PageInfo<FileBean> findInAlbumImg(Integer page,Integer limit,String albumid);
	
	/**
	 * 查询没有分配相册的图片分页列表
	 * @param page
	 * @param limit
	 * @param userid
	 * @return
	 */
	public PageInfo<FileBean> findNotInAlbumImg(Integer page,Integer limit,String userid);
	
	/**
	 * 给相册分配图片
	 * @param albumid
	 * @param ids
	 */
	public void setImgToAlbum(String albumid,List<String> ids);
	
	/**
	 * 移出图片出相册
	 * @param fileid
	 */
	public void removeImgOutAlbum(String albumid,List<String> fileids);
	
	/**
	 * 移动图片
	 * @param albumid
	 * @param fileid
	 */
	public void moveImgToAlbum(String albumid,List<String> fileids);
	/**
	 * 给相册设置封面
	 * @param albumid
	 * @param fileid
	 */
	public void setAlbumCover(String albumid,String fileid);
	/**
	 * 删除相册封面
	 * @param albumid
	 */
	public void deleteAlbumCover(String albumid);
}

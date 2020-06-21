package com.micro.disk.service;

import java.util.List;
import com.micro.disk.bean.AppFileBean;
import com.micro.disk.bean.PageInfo;

public interface AppFileService {
	/**
	 * 通过filemd5检查文件是否存在【用于实现秒传】
	 * @param filemd5
	 * @return
	 */
	public Integer checkFileByMd5(String filemd5);
	
	/**
	 * 切块上传
	 * @param appId
	 * @param bytes 字节数组
	 * @param filemd5 文件md5
	 * @param filename 文件名称
	 * @param chunknum 切块序号
	 * @param userid 用户ID
	 */
	public void uploadChunk(byte[] bytes,String appId,String filemd5,String filename,int chunknum,String userid);
	
	/**
	 * 合并切块
	 * @param appId
	 * @param filemd5
	 * @param filename
	 * @param filesize
	 * @param businessid
	 * @param businesstype
	 * @param userid
	 * @param username
	 * @param secondUpload
	 * @param allowMultiple
	 */
	public String mergeChunk(String appId,String filemd5,String filename,long filesize,
			String businessid,String businesstype,String userid,String username,Boolean secondUpload,Boolean allowMultiple);
	
	/**
	 * 文件有损坏
	 * @param appId
	 * @param businessid
	 * @param businesstype
	 * @param filemd5
	 */
	public void fileHasBreak(String appId,String businessid,String businesstype,String filemd5);
	
	/**
	 * 文件在线编辑
	 * @param bytes
	 * @param fileid 文件ID
	 */
	public void editFile(byte[] bytes,String fileid);
	
	/**
	 * 分页查询
	 * @param page
	 * @param limit
	 * @param appid
	 * @param userid
	 * @param username
	 * @param filename
	 * @param filemd5
	 * @param isAdmin
	 * @return
	 */
	public PageInfo<AppFileBean> findFiles(Integer page,Integer limit,
			String appid,String userid,String username,String filename,String filemd5,Boolean isAdmin);
	
	/**
	 * 根据businessid查询业务系统文件
	 * @param businessid
	 * @return
	 */
	public List<AppFileBean> findFiles(String appId,String businessid);
	
	/**
	 * 根据businessid+businesstype查询业务系统文件
	 * @param businessid
	 * @param businesstype
	 * @return
	 */
	public List<AppFileBean> findFiles(String appId,String businessid,String businesstype);
	
	/**
	 * 删除文件
	 * @param fileid
	 */
	public void delete(String fileid);

	/**
	 * 是否支持该格式
	 * @param filesuffix
	 * @return
	 */
	public boolean hasSupportSuffix(String filesuffix);
	
	/**
	 * 个人网盘->应用网盘
	 * @param appId
	 * @param businessId
	 * @param businessType
	 * @param fileId
	 * @param userId
	 * @param userName
	 */
	public void saveToApplNetdisk(String appId,String businessId,String businessType,String fileId,String userId,String userName);
}

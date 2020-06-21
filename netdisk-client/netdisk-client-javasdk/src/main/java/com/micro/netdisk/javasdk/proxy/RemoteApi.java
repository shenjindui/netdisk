package com.micro.netdisk.javasdk.proxy;

import java.util.List;

import com.micro.netdisk.javasdk.bean.ApplicationFilePojo;
import com.micro.netdisk.javasdk.bean.PageBean;
import com.micro.netdisk.javasdk.bean.PersonalFilePojo;

/**
 * 把FileService业务逻辑和服务调用解耦，做一层中间层
 * @author Administrator
 *
 */
public interface RemoteApi {
	/**
	 * 【检查md5】通过filemd5检查文件是否存在
	 * @param appId
	 * @param filemd5
	 * @return
	 */
	public Integer checkFileByMd5(String appId,String filemd5);
	
	/**
	 * 切块上传
	 * @param bytes
	 * @param appId
	 * @param filemd5
	 * @param filename
	 * @param chunknum
	 * @param userid
	 */
	public void uploadChunk(byte[] bytes,String appId,String filemd5,String filename,long chunknum,String userid);
	
	/**
	 * 切块合并
	 * @param appId
	 * @param filemd5
	 * @param filename
	 * @param filesize
	 * @param businessid
	 * @param businesstype
	 * @param userid
	 * @param username
	 * @param secondUpload 是否秒传
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
	 * 根据APPID进行分页查询
	 * @param page
	 * @param limit
	 * @param appId
	 * @return
	 */
	public PageBean<ApplicationFilePojo> findFilesByAppid(Integer page,Integer limit,String appId);
	
	/**
	 * 根据appId和userid进行分页查询
	 * @param page
	 * @param limit
	 * @param appId
	 * @param userid
	 * @return
	 */
	public PageBean<ApplicationFilePojo> findFilesByUserid(Integer page,Integer limit,String appId,String userId);
	/**
	 * 根据businessid查询业务系统文件
	 * @param appId
	 * @param businessId
	 * @return
	 */
	public List<ApplicationFilePojo> findFiles(String appId,String businessId);
	/**
	 * 根据businessid+businesstype查询业务系统文件
	 * @param appId
	 * @param businessId
	 * @param businessType
	 * @return
	 */
	public List<ApplicationFilePojo> findFiles(String appId,String businessId,String businessType);
	/**
	 * 【删除】
	 * @param appId
	 * @param fileId
	 */
	public void delete(String appId,String fileId);
	/**
	 * 【下载】根据fileid获取List<String>【getChunkStoreList+getBytesByStorepath配套使用】
	 * @param appId
	 * @param fileId
	 * @return
	 */
	public List<String> getChunkStorepathList(String appId,String fileId);
	/**
	 * 【下载】根据chunkstorepath获取字节流【getChunkStoreList+getBytesByStorepath配套使用】
	 * @param appId
	 * @param storePath
	 * @return
	 */
	public byte[] getBytesByStorepath(String appId,String storePath);
	/**
	 * 查询个人网络下的文件夹树（懒加载）
	 * @param appId
	 * @param userId 用户ID
	 * @param folderId 目录ID，缺失则为0
	 * @return
	 */
	public List<PersonalFilePojo> findPersonalFiles(String appId,String userId,String folderId);
	/**
	 * 创建个人文件夹
	 * @param appId
	 * @param userId 用户ID
	 * @param userName 用户名
	 * @param parentFolderId 所属父目录ID
	 * @param folderName 文件夹名称
	 */
	public void mkdirInPersonalNetdisk(String appId,String userId,String userName,String parentFolderId,String folderName);
	/**
	 * 应用网盘->个人网盘
	 * @param appId
	 * @param fileId 文件ID
	 * @param targetFolderId 目标文件夹ID
	 * @param userId 用户ID
	 * @param userName 用户名称
	 */
	public void saveToPersonalNetdisk(String appId,String fileId,String targetFolderId,String userId,String userName);
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

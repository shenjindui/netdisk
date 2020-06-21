package com.micro.netdisk.javasdk.service;

import java.util.List;
import com.micro.netdisk.javasdk.bean.ApplicationFilePojo;
import com.micro.netdisk.javasdk.bean.PageBean;
import com.micro.netdisk.javasdk.bean.PersonalFilePojo;

public interface FileService {

	/**
	 * 【检查md5】通过filemd5检查文件是否存在
	 * @param appId
	 * @param filemd5
	 * @return
	 */
	public Integer checkFileByMd5(String appId,String filemd5);
	
	/**
	 * 【秒传】前端根据md5查询，如何发现其已经存在，则实现秒传
	 * @param appId
	 * @param businessId
	 * @param businessType
	 * @param fileMd5
	 * @param fileName
	 * @param fileSize
	 * @param userId
	 * @param userName
	 * @param allowMultiple
	 * return 返回文件ID
	 */
	public String secondUpload(String appId,String businessId,String businessType,
			String fileMd5,String fileName,long fileSize,String userId,String userName,Boolean allowMultiple);
	
	/**
	 * 【上传】业务系统先把文件存储临时目录，客户端再读取临时目录文件进行上传
	 * @param appId
	 * @param tempFilePath 文件临时目录【如果为空，则为秒传】
	 * @param businessId 业务ID（一般是业务主键ID）
	 * @param businessType 业务标识（业务类型，比如：一条记录对应多种文件，正文、参考资料）
	 * @param fileMd5
	 * @param fileName 文件名称
	 * @param userId 上传人ID
	 * @param userName 上传人姓名
	 * @param allowMultiple 是否允许上传多个，Null则为true，如果为false则会覆盖，比如：正文只允许一个
	 * return 返回文件ID
	 */
	public String uploadLocalFile(String appId,String tempFilePath,String businessId,String businessType,
			String fileMd5,String fileName,String userId,String userName,Boolean allowMultiple);
	
	
	/**
	 * 文件在线编辑
	 * @param appId
	 * @param bytes
	 * @param fileid 文件ID
	 */
	public void editFile(String appId,byte[] bytes,String fileid);
	
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
	 * @param userName 用户姓名
	 * @param parentFolderId 所属父目录ID
	 * @param folderName 文件夹名称
	 */
	public void mkdirInPersonalNetdisk(String appId,String userId,String userName,String parentFolderId,String folderName);
	
	/**
	 * 转存，应用网盘->个人网盘
	 * @param appId
	 * @param fileId 文件ID
	 * @param targetFolderId 目标文件夹ID
	 * @param userId 用户ID
	 * @param userName 用户名称
	 */
	public void saveToPersonalNetdisk(String appId,String fileId,String targetFolderId,String userId,String userName);
	/**
	 * 转存，个人网盘->应用网盘
	 * @param appId
	 * @param businessId
	 * @param businessType
	 * @param fileId
	 * @param userId
	 * @param userName
	 */
	public void saveToApplNetdisk(String appId,String businessId,String businessType,String fileId,String userId,String userName);
}

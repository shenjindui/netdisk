package com.micro.disk.service;

import java.util.List;
import java.util.Map;

import com.micro.disk.bean.Chunk;
import com.micro.disk.bean.FileBean;
import com.micro.disk.bean.FileListBean;
import com.micro.disk.bean.FolderProp;
import com.micro.disk.bean.FolderTree;
import com.micro.disk.bean.MergeFileBean;
import com.micro.disk.bean.PageInfo;

public interface FileService {
	/**
	 * 分页列表
	 * @param page
	 * @param limit
	 * @param userid
	 * @param pid
	 * @param typecode
	 * @param orderfield
	 * @param ordertype
	 * @return
	 */
	public PageInfo<FileListBean> findPageList(Integer page,Integer limit,String userid,String pid,String typecode,String orderfield,String ordertype);
	/**
	 * 分页列表【卡片】
	 * @param page
	 * @param limit
	 * @param userid
	 * @param pid
	 * @param typecode
	 * @return
	 */
	public PageInfo<FileListBean> findPageListCard(Integer page,Integer limit,String userid,String pid,String typecode,String orderfield,String ordertype);
	
	/**
	 * 文档、视频、音乐、其他
	 * @param page
	 * @param limit
	 * @param userid
	 * @param typecode
	 * @param filesuffix
	 * @param filename
	 * @param showtype
	 * @param orderfield
	 * @param ordertype
	 * @return
	 */
	public PageInfo<FileListBean> findSpecialList(Integer page,Integer limit,String userid,String typecode,String filesuffix,String filename,String showtype,
			String orderfield,String ordertype);
	
	/**
	 * 查询单条记录
	 * @param id
	 * @return
	 */
	public FileBean findOne(String id);
	/**
	 * 根据pid查询子文件
	 * @param userid
	 * @param pid
	 * @return
	 */
	public List<FileBean> findChildrenFiles(String userid,String pid);
	
	/**
	 * 上传切块
	 * @param chunk
	 */
	public void uploadChunk(Chunk chunk);
	
	/**
	 * 检查切块是否存在
	 * @param filemd5
	 * @return
	 */
	public Integer checkFile(String filemd5);
	
	/**
	 * 检查切块是否存在
	 * @param userid
	 * @param filemd5
	 * @param chunkNumber
	 * @return
	 */
	@Deprecated
	public Integer checkChunk(String userid,String filemd5, Integer chunkNumber);


	/**
	 * 合并切块
	 * @param bean
	 */
	public void mergeChunk(MergeFileBean bean);
	
	/**
	 * 创建文件夹
	 * @param pid
	 * @param filename
	 * @param userid
	 * @param username
	 */
	public void addFolder(String pid,String filename,String userid,String username);
	
	/**
	 * 重命名
	 * @param userid
	 * @param id
	 * @param filename
	 */
	public void rename(String userid,String id,String filename);
	
	/**
	 * 删除
	 * @param createuserid
	 * @param createusername
	 * @param ids
	 */
	public void delete(String createuserid,String createusername,List<String> ids);
	
	/**
	 * 查询文件夹树【懒加载】
	 * @param userid
	 * @param pid 父节点id
	 * @param id  这里主要是控制不展示被复制文件夹
	 * @return
	 */
	@Deprecated
	public List<FolderTree> findFolderTree(String userid,String pid,List<String> ids);
	
	/**
	 * 查询文件夹【复制、移动、上传--选择文件夹】
	 * @param userid
	 * @param pid
	 * @param ids
	 * @return
	 */
	public List<FileBean> findFolderList(String userid,String pid,List<String> ids);
	
	/**
	 * 复制
	 * @param userid
	 * @param username
	 * @param ids
	 * @param folderid
	 */
	public void copyTo(String userid,String username,List<String> ids,String folderid);
	

	/**
	 * 剪切
	 * @param userid
	 * @param ids
	 * @param folderid
	 */
	public void moveTo(String userid,List<String> ids,String folderid);
	
	/**
	 * 转存（从分享当中）
	 * @param userid
	 * @param username
	 * @param folderid
	 * @param shareid
	 * @param ids
	 */
	public void saveFromShare(String userid,String username,String folderid,String shareid,List<String> ids);
	
	/**
	 * 获取父节点集合
	 * @param pid
	 * @return
	 */
	public List<Map<String,Object>> findParentListByPid(String pid);
	/**
	 * 获取父节点集合
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> findParentListById(String id);

	/**
	 * 文件夹格式
	 * @param userid
	 * @param id
	 * @return
	 */
	public FolderProp findFolderProp(String userid,String id);
	
	/**
	 * 编辑文件
	 * @param fileid
	 * @param bytes
	 */
	public FileBean editFile(String fileid,byte[] bytes);
	
	/**
	 * 创建文件
	 * @param pid
	 * @param filename
	 * @param bytes
	 * @param userid
	 * @param username
	 */
	public FileBean addFile(String pid,String filename,byte[] bytes,String userid,String username);
	
	/**
	 * 从应用文件转存到个人文件
	 * @param fileId
	 * @param targetFolderId
	 * @param userId
	 * @param userName
	 */
	public void addFromAppFile(String fileId,String targetFolderId,String userId,String userName);
}

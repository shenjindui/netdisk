package com.micro.controller;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.ValidateUtils;
import com.micro.disk.service.AppFileService;
import com.micro.disk.service.FilePreviewService;
import com.micro.disk.service.FileService;
import com.micro.idempotence.NoRepeat;

@RestController
@RequestMapping("/netdisk/openapi")
public class FileController {
	@Reference(check=false)
	private AppFileService appFileService;
	@Reference(check=false)
	private FilePreviewService filePreviewService;
	@Reference(check=false)
	private FileService fileService;
	
	/**
	 * 检查MD5是否存在
	 * @param appId
	 * @param filemd5
	 * @return
	 */
	@PostMapping("/checkFileByMd5")
	public Result checkFileByMd5(String appId,String filemd5){
		try{
			ValidateUtils.validate(appId, "appId");
			ValidateUtils.validate(filemd5, "filemd5");
			Integer count=appFileService.checkFileByMd5(filemd5);
			return ResultUtils.success("查询成功",count);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 切块上传
	 * @param file
	 * @param appId
	 * @param filemd5
	 * @param filename
	 * @param chunknum
	 * @param userid
	 * @return
	 */
	@PostMapping("/uploadChunk")
	public Result uploadChunk(MultipartFile file,String appId,String filemd5,String filename,long chunknum,String userId){
		try{
			if(file==null){
				throw new RuntimeException("byte[]为空");
			}
			
			ValidateUtils.validate(appId, "appId");
			ValidateUtils.validate(filemd5, "filemd5");
			ValidateUtils.validate(filename, "filename");
			ValidateUtils.validate(chunknum, "chunknum");
			ValidateUtils.validate(userId, "userid");
			//格式支持
			String filesuffix=FilenameUtils.getExtension(filename);
			boolean flag=appFileService.hasSupportSuffix(filesuffix);
			if(!flag){
				throw new RuntimeException("不支持格式为"+filesuffix+"的文件上传,请联系管理员");
			}
			//合并
			appFileService.uploadChunk(file.getBytes(), appId, filemd5, filename, (int)chunknum, userId);
			return ResultUtils.success("上传切块成功",null);
		}catch(Exception e){
			e.printStackTrace();
			return ResultUtils.error(e.getMessage());
		}
	}
	
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
	 * @param allowMultiple
	 * @param secondUpload 是否为秒传
	 * @return
	 */
	@PostMapping("/mergeChunk")
	//接口幂等性（上传大文件的时候，容易导致重复执行）
	//@NoRepeat
	public Result mergeChunk(String appId,String filemd5,String filename,long filesize,
			String businessId,String businessType,String userId,String userName,Boolean secondUpload,Boolean allowMultiple){
		try{
			ValidateUtils.validate(appId, "appId");
			ValidateUtils.validate(filemd5, "filemd5");
			ValidateUtils.validate(filename, "filename");
			ValidateUtils.validate(filesize, "filesize");
			ValidateUtils.validate(businessId, "businessid");
			ValidateUtils.validate(businessType, "businesstype");
			ValidateUtils.validate(userId, "userid");
			ValidateUtils.validate(userName, "username");
			
			String fileid=appFileService.mergeChunk(appId, filemd5, filename, filesize, 
					businessId, businessType, userId, userName,secondUpload, allowMultiple);
			return ResultUtils.success("合并切块成功",fileid);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 文件有损坏
	 * @param appId
	 * @param businessid
	 * @param businesstype
	 * @param filemd5
	 * @return
	 */
	@PostMapping("/fileHasBreak")
	public Result fileHasBreak(String appId,String businessId,String businessType,String filemd5){
		try{
			ValidateUtils.validate(appId, "appId");
			ValidateUtils.validate(filemd5, "filemd5");
			ValidateUtils.validate(businessId, "businessid");
			ValidateUtils.validate(businessType, "businesstype");
			
			appFileService.fileHasBreak(appId, businessId, businessType, filemd5);
			
			return ResultUtils.success("操作成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param limit
	 * @param appid
	 * @param userid
	 * @return
	 */
	@PostMapping("/findPageFiles")
	public Result findPageFiles(Integer page,Integer limit,String appId,String userId){
		try{
			String username="";
			String filename="";
			String filemd5="";
			return ResultUtils.success("查询成功",appFileService.findFiles(page, limit, appId, userId, username, filename, filemd5,false));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	/**
	 * 根据businessid查询业务系统文件
	 * @param appId
	 * @param businessid
	 * @return
	 */
	@PostMapping("/findFilesByBusinessId")
	public Result findFilesByBusinessId(String appId,String businessId){
		try{
			
			return ResultUtils.success("查询成功",appFileService.findFiles(appId, businessId));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	/**
	 * 根据businessid+businesstype查询业务系统文件
	 * @param appId
	 * @param businessid
	 * @param businesstype
	 * @return
	 */
	@PostMapping("/findFilesByBusissIdAndType")
	public Result findFilesByBusissIdAndType(String appId,String businessId,String businessType){
		try{
			return ResultUtils.success("查询成功",appFileService.findFiles(appId, businessId, businessType));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	/**
	 * 删除
	 * @param appId
	 * @param fileId
	 * @return
	 */
	@PostMapping("/delete")
	public Result delete(String appId,String fileId){
		try{
			ValidateUtils.validate(appId, "appId");
			ValidateUtils.validate(fileId, "fileId");
			
			appFileService.delete(fileId);
			
			return ResultUtils.success("删除成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	/**
	 * 根据fileid获取List<String>
	 * @param appId
	 * @param fileId
	 * @return
	 */
	@PostMapping("/getChunkStorepathList")
	public Result getChunkStorepathList(String appId,String fileId){
		try{
			return ResultUtils.success("查询成功",filePreviewService.getChunksByAppFileid(fileId));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 根据chunkstorepath获取字节流
	 * @param appId
	 * @param storePath
	 * @param response
	 */
	@PostMapping("getBytesByStorepath")
	public void getBytesByStorepath(String appId,String storePath,HttpServletResponse response){
		OutputStream out=null;
		try {
			byte[] bytes=filePreviewService.getBytesByUrl(storePath);
			out=response.getOutputStream();
			out.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 查询个人网络下的文件夹树（懒加载）
	 * @param appId
	 * @param userId
	 * @param folderId
	 * @return
	 */
	@PostMapping("/findPersonalFiles")
	public Result findPersonalFiles(String appId,String userId,String folderId){
		try{
			return ResultUtils.success("查询成功",fileService.findChildrenFiles(userId, folderId));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	/**
	 * 创建个人文件夹
	 * @param appId
	 * @param userId
	 * @param userName
	 * @param parentFolderId
	 * @param folderName
	 * @return
	 */
	@PostMapping("/mkdirInPersonalNetdisk")
	public Result mkdirInPersonalNetdisk(String appId,String userId,String userName,String parentFolderId,String folderName){
		try{
			fileService.addFolder(parentFolderId, folderName, userId, userName);
			return ResultUtils.success("创建成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	/**
	 * 应用网盘->个人网盘
	 * @param appId
	 * @param fileId
	 * @param targetFolderId
	 * @param userId
	 * @param userName
	 * @return
	 */
	@PostMapping("/saveToPersonalNetdisk")
	public Result saveToPersonalNetdisk(String appId,String fileId,String targetFolderId,String userId,String userName){
		try{
			fileService.addFromAppFile(fileId, targetFolderId, userId, userName);
			return ResultUtils.success("转存成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 个人网盘->应用网盘
	 * @param appId
	 * @param businessId
	 * @param businessType
	 * @param fileId
	 * @param userId
	 * @param userName
	 * @return
	 */
	@PostMapping("/saveToApplNetdisk")
	public Result saveToApplNetdisk(String appId,String businessId,String businessType,String fileId,String userId,String userName){
		try{
			appFileService.saveToApplNetdisk(appId, businessId, businessType, fileId, userId, userName);
			return ResultUtils.success("转存成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

package com.micro.netdisk.javasdk.validate;

import java.util.List;

import com.micro.netdisk.javasdk.bean.PersonalFilePojo;
import com.micro.netdisk.javasdk.proxy.RemoteApi;
import com.micro.netdisk.javasdk.proxy.RemoteApiProxy;

public class ParamValidateUtils {
	public static void checkFileByMd5(String appId, String filemd5){
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(filemd5==null||"".equals(filemd5)){
			throw new RuntimeException("filemd5不能为空");
		}
	}
	public static void secondUpload(String appId,String fileMd5,String fileName,String businessId,String businessType,String userId,String userName){
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(fileMd5==null||"".equals(fileMd5)){
			throw new RuntimeException("fileMd5不能为空");
		}
		if(fileName==null||"".equals(fileName)){
			throw new RuntimeException("fileName不能为空");
		}
		if(businessId==null||"".equals(businessId)){
			throw new RuntimeException("businessId不能为空");
		}
		if(businessType==null||"".equals(businessType)){
			throw new RuntimeException("businessType不能为空");
		}
		if(userId==null||"".equals(userId)){
			throw new RuntimeException("userId不能为空");
		}
		if(userName==null||"".equals(userName)){
			throw new RuntimeException("userName不能为空");
		}
	}
	public static void uploadLocalFile(String appId, String tempFilePath, String businessId, String businessType,
			String fileMd5, String fileName, String userId, String userName){
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(tempFilePath==null||"".equals(tempFilePath)){
			throw new RuntimeException("tempFilePath不能为空");
		}
		if(businessId==null||"".equals(businessId)){
			throw new RuntimeException("businessId不能为空");
		}
		if(businessType==null||"".equals(businessType)){
			throw new RuntimeException("businessType不能为空");
		}
		if(fileMd5==null||"".equals(fileMd5)){
			throw new RuntimeException("fileMd5不能为空");
		}
		if(fileName==null||"".equals(fileName)){
			throw new RuntimeException("fileName不能为空");
		}
		if(userId==null||"".equals(userId)){
			throw new RuntimeException("userId不能为空");
		}
		if(userName==null||"".equals(userName)){
			throw new RuntimeException("userName不能为空");
		}
	}
	public static void findPersonalFiles(String appId, String userId, String folderId) {
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(userId==null||"".equals(userId)){
			throw new RuntimeException("userId不能为空");
		}
		if(folderId==null||"".equals(folderId)){
			throw new RuntimeException("folderId不能为空");
		}
	}
	public static void mkdirInPersonalNetdisk(String appId, String userId, String parentFolderId, String folderName) {
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(userId==null||"".equals(userId)){
			throw new RuntimeException("userId不能为空");
		}
		if(parentFolderId==null||"".equals(parentFolderId)){
			throw new RuntimeException("parentFolderId不能为空");
		}
		if(folderName==null||"".equals(folderName)){
			throw new RuntimeException("folderName不能为空");
		}
	}
	public static void saveToPersonalNetdisk(String appId, String fileId, String targetFolderId, String userId,
			String userName) {
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(fileId==null||"".equals(fileId)){
			throw new RuntimeException("fileId不能为空");
		}
		if(targetFolderId==null||"".equals(targetFolderId)){
			throw new RuntimeException("targetFolderId不能为空");
		}
		if(userId==null||"".equals(userId)){
			throw new RuntimeException("userId不能为空");
		}
		if(userName==null||"".equals(userName)){
			throw new RuntimeException("userName不能为空");
		}
	}
	public static void saveToApplNetdisk(String appId, String businessId, String businessType, String fileId, String userId,String userName) {
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(businessId==null||"".equals(businessId)){
			throw new RuntimeException("businessId不能为空");
		}
		if(businessType==null||"".equals(businessType)){
			throw new RuntimeException("businessType不能为空");
		}
		if(fileId==null||"".equals(fileId)){
			throw new RuntimeException("fileId不能为空");
		}
		if(userId==null||"".equals(userId)){
			throw new RuntimeException("userId不能为空");
		}
		if(userName==null||"".equals(userName)){
			throw new RuntimeException("userName不能为空");
		}
	}
	public static void getBytesByStorepath(String appId, String storePath) {
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(storePath==null||"".equals(storePath)){
			throw new RuntimeException("storePath不能为空");
		}
	}
	public static void getChunkStorepathList(String appId, String fileId){
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(fileId==null||"".equals(fileId)){
			throw new RuntimeException("fileId不能为空");
		}
	}
	public static void delete(String appId, String fileId){
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(fileId==null||"".equals(fileId)){
			throw new RuntimeException("fileId不能为空");
		}
	}
	public static void findFiles(String appId, String businessId, String businessType){
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(businessId==null||"".equals(businessId)){
			throw new RuntimeException("businessId不能为空");
		}
		if(businessType==null||"".equals(businessType)){
			throw new RuntimeException("businessType不能为空");
		}
	}
	public static void findFiles(String appId, String businessId){
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(businessId==null||"".equals(businessId)){
			throw new RuntimeException("businessId不能为空");
		}
	}
	public static void findFilesByUserid(Integer page,Integer limit,String appId, String userId){
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
		if(userId==null||"".equals(userId)){
			throw new RuntimeException("userId不能为空");
		}
	}
	public static void findFilesByAppid(Integer page,Integer limit,String appId){
		if(appId==null||"".equals(appId)){
			throw new RuntimeException("appId不能为空");
		}
	}
}

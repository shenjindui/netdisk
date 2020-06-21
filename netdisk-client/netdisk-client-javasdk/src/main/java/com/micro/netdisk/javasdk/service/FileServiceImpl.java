package com.micro.netdisk.javasdk.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.micro.netdisk.javasdk.bean.ApplicationFilePojo;
import com.micro.netdisk.javasdk.bean.PageBean;
import com.micro.netdisk.javasdk.bean.PersonalFilePojo;
import com.micro.netdisk.javasdk.proxy.RemoteApi;
import com.micro.netdisk.javasdk.proxy.RemoteApiProxy;
import com.micro.netdisk.javasdk.thread.ChunkDealThread;
import com.micro.netdisk.javasdk.thread.Md5CalThread;
import com.micro.netdisk.javasdk.validate.ParamValidateUtils;

public class FileServiceImpl implements FileService{
	private long size=500*1024*1024;//500，大于500开启线程计算MD5
	private long chunksize=1*1024*1024;//1M，切块标准
	private ExecutorService es=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private ExecutorService esMd5=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	//不让用户new
	//private FileServiceImpl(){}

	@Override
	public Integer checkFileByMd5(String appId, String filemd5) {
		//校验
		ParamValidateUtils.checkFileByMd5(appId, filemd5);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		return remoteApi.checkFileByMd5(appId, filemd5);
	}

	//如果业务系统的【前端】检测到md5已经存在，则实现秒传
	@Override
	public String secondUpload(String appId, String businessId, String businessType, String fileMd5, String fileName,
			long fileSize, String userId, String userName, Boolean allowMultiple) {
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		//校验
		ParamValidateUtils.secondUpload(appId, fileMd5, fileName, businessId, businessType, userId, userName);
		if(allowMultiple==null){
			allowMultiple=true;
		}
		//接口调用
		return remoteApi.mergeChunk(appId, fileMd5, fileName, fileSize, businessId, businessType, userId, userName,true, allowMultiple);
	}

	@Override
	public String uploadLocalFile(String appId, String tempFilePath, String businessId, String businessType,
			String fileMd5, String fileName, String userId, String userName, Boolean allowMultiple) {
		FileInputStream fis=null;
		FileChannel fileChannel=null;
		Future<String> future=null;
		File file=null;
		Md5CalThread thread=null;
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		String fileid="";
		try{
			//1.校验参数
			ParamValidateUtils.uploadLocalFile(appId, tempFilePath, businessId, businessType, fileMd5, fileName, userId, userName);
			//2.获取文件大小
			file=new File(tempFilePath);
			fis=new FileInputStream(file);
			long filesize=file.length();
			
			//3.计算MD5
			thread=new Md5CalThread(file);
			future=esMd5.submit(thread);
			String md5=future.get();
			if("".equals(md5)){
				throw new RuntimeException("计算MD5失败");
			}else{
				if(!fileMd5.equals(md5)){
					throw new RuntimeException("传递的MD5和计算的MD5不一致");						
				}
			}
			
			//4.校验md5是否已经存在
			int returnNum=remoteApi.checkFileByMd5(appId, fileMd5);
			if(returnNum>0){
				//直接合并切块，无需再上传文件
				fileid=remoteApi.mergeChunk(appId, fileMd5, fileName, filesize, businessId, businessType, userId, userName,true, allowMultiple);
			}else{				
				//5.计算切块数
				long count=0;
				if(filesize<chunksize){
					count=1;
				}else{
					count=(filesize%chunksize)==0?(filesize/chunksize):(filesize/chunksize)+1;
				}
				CountDownLatch cdl=new CountDownLatch((int)count);
				
				//6.获取切块数组，并且子线程去处理
				ByteBuffer buffer=ByteBuffer.allocate((int)chunksize);
				fileChannel=fis.getChannel();
				int chunkNum=0;
				while(fileChannel.read(buffer)>0){
					//如果很多人同时上传大文件，切块数太大，超出了线程池的队列范围，会执行拒绝策略，到时候，文件上传失败【暂时不考虑】
					//获取字节数组
					buffer.flip();
					byte[] dst=new byte[buffer.limit()];
					buffer.get(dst, buffer.position(), buffer.limit());
					buffer.clear();
					
					//同步方式上传切块
					//remoteApi.uploadChunk(dst, appId, fileMd5, fileName, chunkNum, userId);
					
					//异步方式上传切块
					final int numThread=chunkNum;
					es.submit(new ChunkDealThread(cdl,dst, appId, fileMd5, fileName, numThread, userId));
					
					//处理完成递增
					chunkNum++;
				}
				
				//7.所有切块正常才合并切块，如何有异常，则通知后台情况Redis和切块文件
				cdl.await();
				fileid=remoteApi.mergeChunk(appId, fileMd5, fileName, filesize, businessId, businessType, userId, userName,false, allowMultiple);
			}
			return fileid;
		}catch(Exception e){
			throw new RuntimeException("上传错误："+e.getMessage());
		}finally{
			//关闭流
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileChannel!=null){
				try {
					fileChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//删除
			if(file!=null&&file.exists()){
				file.delete();
			}
		}
	}

	@Override
	public void editFile(String appId, byte[] bytes, String fileid) {
		
	}

	@Override
	public PageBean<ApplicationFilePojo> findFilesByAppid(Integer page,Integer limit,String appId) {
		//校验
		ParamValidateUtils.findFilesByAppid(page, limit, appId);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		return remoteApi.findFilesByAppid(page,limit,appId);
	}

	@Override
	public PageBean<ApplicationFilePojo> findFilesByUserid(Integer page,Integer limit,String appId, String userId) {
		//校验
		ParamValidateUtils.findFilesByUserid(page, limit, appId, userId);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		return remoteApi.findFilesByUserid(page,limit,appId, userId);
	}

	@Override
	public List<ApplicationFilePojo> findFiles(String appId, String businessId) {
		//校验
		ParamValidateUtils.findFiles(appId, businessId);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		return remoteApi.findFiles(appId, businessId);
	}

	@Override
	public List<ApplicationFilePojo> findFiles(String appId, String businessId, String businessType) {
		//校验
		ParamValidateUtils.findFiles(appId, businessId, businessType);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		return remoteApi.findFiles(appId, businessId, businessType);
	}

	@Override
	public void delete(String appId, String fileId) {
		//校验
		ParamValidateUtils.delete(appId, fileId);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		remoteApi.delete(appId, fileId);
	}

	@Override
	public List<String> getChunkStorepathList(String appId, String fileId) {
		//校验
		ParamValidateUtils.getChunkStorepathList(appId, fileId);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		return remoteApi.getChunkStorepathList(appId, fileId);
	}

	@Override
	public byte[] getBytesByStorepath(String appId, String storePath) {
		//校验
		ParamValidateUtils.getBytesByStorepath(appId, storePath);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		return remoteApi.getBytesByStorepath(appId, storePath);
	}

	@Override
	public List<PersonalFilePojo> findPersonalFiles(String appId, String userId, String folderId) {
		//校验
		ParamValidateUtils.findPersonalFiles(appId, userId, folderId);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		return remoteApi.findPersonalFiles(appId, userId, folderId);
	}

	@Override
	public void mkdirInPersonalNetdisk(String appId, String userId,String userName, String parentFolderId, String folderName) {
		//校验
		ParamValidateUtils.mkdirInPersonalNetdisk(appId, userId, parentFolderId, folderName);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		remoteApi.mkdirInPersonalNetdisk(appId, userId,userName, parentFolderId, folderName);
	}

	@Override
	public void saveToPersonalNetdisk(String appId, String fileId, String targetFolderId, String userId,
			String userName) {
		//校验
		ParamValidateUtils.saveToPersonalNetdisk(appId, fileId, targetFolderId, userId, userName);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		remoteApi.saveToPersonalNetdisk(appId, fileId, targetFolderId, userId, userName);
	}

	@Override
	public void saveToApplNetdisk(String appId, String businessId, String businessType, String fileId, String userId,
			String userName) {
		//校验
		ParamValidateUtils.saveToApplNetdisk(appId, businessId, businessType, fileId, userId, userName);
		//接口调用
		RemoteApi remoteApi=RemoteApiProxy.getInstance();
		remoteApi.saveToApplNetdisk(appId, businessId, businessType, fileId, userId, userName);
	}
}

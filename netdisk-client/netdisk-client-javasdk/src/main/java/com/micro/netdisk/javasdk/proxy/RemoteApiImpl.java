package com.micro.netdisk.javasdk.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.micro.netdisk.javasdk.bean.ApplicationFilePojo;
import com.micro.netdisk.javasdk.bean.PageBean;
import com.micro.netdisk.javasdk.bean.PersonalFilePojo;
import com.micro.netdisk.javasdk.bean.Result;
import com.micro.netdisk.javasdk.transport.HttpRequest;
import com.micro.netdisk.javasdk.transport.RpcResponse;
import com.micro.netdisk.javasdk.transport.TransportContext;

public class RemoteApiImpl implements RemoteApi{
	private String ROOT="netdisk/openapi";
	@Override
	public Integer checkFileByMd5(String appId, String filemd5) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("filemd5", filemd5);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/checkFileByMd5");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			byte[] bytes=res.getData();
			Result result=JSON.parseObject(bytes, Result.class);
			if(result.getCode()==0){
				return Integer.parseInt(result.getData().toString());
			}else{				
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}
	
	@Override
	public void uploadChunk(byte[] bytes, String appId, String filemd5, String filename, long chunknum, String userid) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("filemd5", filemd5);
		param.put("filename", filename);
		param.put("chunknum", chunknum+"");
		param.put("userId", userid);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/uploadChunk");
		req.setParam(param);
		req.setReceiveName("file");
		req.setFileName(filename);
		req.setBytes(bytes);
		
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==1){
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}		
	}

	@Override
	public String mergeChunk(String appId, String filemd5, String filename, long filesize, String businessid,
			String businesstype, String userid, String username,Boolean secondUpload, Boolean allowMultiple) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("filemd5", filemd5);
		param.put("filename", filename);
		param.put("filesize", filesize+"");
		param.put("businessId", businessid);
		param.put("businessType", businesstype);
		param.put("userId", userid);
		param.put("userName", username);
		param.put("secondUpload", secondUpload==true?"true":"false");
		param.put("allowMultiple", allowMultiple==true?"true":"false");
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/mergeChunk");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==0){
				return result.getData().toString();
			}else{				
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}
	
	@Override
	public void fileHasBreak(String appId, String businessid, String businesstype, String filemd5) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("businessId", businessid);
		param.put("businessType", businesstype);
		param.put("filemd5", filemd5);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/fileHasBreak");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==1){
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}		
	}
	
	@Override
	public PageBean<ApplicationFilePojo> findFilesByAppid(Integer page,Integer limit,String appId) {
		Map<String,String> param=new HashMap<>();
		param.put("page", page+"");
		param.put("limit", limit+"");
		param.put("appId", appId);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/findPageFiles");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==0){
				PageBean<ApplicationFilePojo> pb=new PageBean<>();
				Object obj=result.getData();
				pb=JSON.parseObject(obj.toString(), PageBean.class);
				return pb;
			}else{				
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}

	@Override
	public PageBean<ApplicationFilePojo> findFilesByUserid(Integer page,Integer limit,String appId, String userId) {
		Map<String,String> param=new HashMap<>();
		param.put("page", page+"");
		param.put("limit", limit+"");
		param.put("appId", appId);
		param.put("userId", userId);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/findPageFiles");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==0){
				PageBean<ApplicationFilePojo> pb=new PageBean<>();
				Object obj=result.getData();
				pb=JSON.parseObject(obj.toString(), PageBean.class);
				return pb;
			}else{				
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}

	@Override
	public List<ApplicationFilePojo> findFiles(String appId, String businessId) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("businessId", businessId);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/findFilesByBusinessId");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==0){
				return (List<ApplicationFilePojo>) result.getData();
			}else{				
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}

	@Override
	public List<ApplicationFilePojo> findFiles(String appId, String businessId, String businessType) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("businessId", businessId);
		param.put("businessType", businessType);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/findFilesByBusissIdAndType");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==0){
				return (List<ApplicationFilePojo>) result.getData();
			}else{				
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}

	@Override
	public void delete(String appId, String fileId) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("fileId", fileId);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/delete");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==1){
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}		
	}

	@Override
	public List<String> getChunkStorepathList(String appId, String fileId) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("fileId", fileId);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/getChunkStorepathList");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==0){
				return (List<String>) result.getData();
			}else{				
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}

	@Override
	public byte[] getBytesByStorepath(String appId, String storePath) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("storePath", storePath);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/getBytesByStorepath");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			return res.getData();
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}

	@Override
	public List<PersonalFilePojo> findPersonalFiles(String appId, String userId, String folderId) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("userId", userId);
		param.put("folderId", folderId);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/findPersonalFiles");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==0){
				return (List<PersonalFilePojo>) result.getData();
			}else{				
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}

	@Override
	public void mkdirInPersonalNetdisk(String appId, String userId,String userName, String parentFolderId, String folderName) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("userId", userId);
		param.put("userName", userName);
		param.put("parentFolderId", parentFolderId);
		param.put("folderName", folderName);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/mkdirInPersonalNetdisk");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==1){
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}		
	}

	@Override
	public void saveToPersonalNetdisk(String appId, String fileId, String targetFolderId, String userId,
			String userName) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("fileId", fileId);
		param.put("targetFolderId", targetFolderId);
		param.put("userId", userId);
		param.put("userName", userName);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/saveToPersonalNetdisk");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==1){
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}

	@Override
	public void saveToApplNetdisk(String appId, String businessId, String businessType, String fileId, String userId,
			String userName) {
		Map<String,String> param=new HashMap<>();
		param.put("appId", appId);
		param.put("businessId", businessId);
		param.put("businessType", businessType);
		param.put("fileId", fileId);
		param.put("userId", userId);
		param.put("userName", userName);
		
		HttpRequest req=new HttpRequest();
		req.setRequrl(ROOT+"/saveToApplNetdisk");
		req.setParam(param);
		RpcResponse res=TransportContext.sendMsg(req);
		if(res.getCode()==0){
			Result result=JSON.parseObject(res.getData(), Result.class);
			if(result.getCode()==1){
				throw new RuntimeException(result.getMsg());
			}
		}else{
			throw new RuntimeException(res.getMsg());
		}
	}

	
}

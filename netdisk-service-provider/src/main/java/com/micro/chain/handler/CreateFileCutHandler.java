package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.CreateRequest;
import com.micro.common.Contanst;
import com.micro.modeltree.FileChunk;
import com.micro.store.context.StoreContext;
import com.micro.store.service.StoreService;

@Component
public class CreateFileCutHandler extends Handler{
	@Autowired
	private StoreContext storeContext;
	
	@NacosValue(value="${chunksize}",autoRefreshed=true)
	private int chunksize;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof CreateRequest){
			CreateRequest bean=(CreateRequest) request;
			if(!bean.isExist()){
				//切分切块
				String filename=bean.getFilename();
				String filemd5=bean.getFilemd5();
				byte[] bytes=bean.getBytes();
				List<FileChunk> chunks=cutFile(filename, filemd5, bytes);
				
				//上传切块
				storeChunk(chunks);
				
				//写到下一个Hanlder
				bean.setChunks(chunks);
				this.updateRequest(bean);
			}
		}else{
			throw new RuntimeException("CreateFileCutHandler==参数不对");
		}		
	}
	public void storeChunk(List<FileChunk> chunks){
		List<String> paths=new ArrayList<String>();
		try{			
			if(!CollectionUtils.isEmpty(chunks)){
				for(FileChunk chunk:chunks){
					String storepath=storeContext.upload("", chunk.getBytes(), chunk.getChunkname());
					chunk.setStorepath(storepath);
					paths.add(storepath);
				}
			}		
		}catch(Exception e){
			if(!CollectionUtils.isEmpty(paths)){
				for(String path:paths){
					storeContext.delete(path);
				}
			}
			throw new RuntimeException(e.getMessage());
		}
	}
	private List<FileChunk> cutFile(String filename,String filemd5,byte[] bytes){
		int len=bytes.length;
		List<FileChunk> chunks=new ArrayList<FileChunk>();
		if(len>chunksize){
			int count=(len%chunksize)==0?(len/chunksize):(len/chunksize)+1;
			int mod=len%chunksize;
			for(int i=0;i<count;i++){
				int start=0;
				int end=0;
				if(i<(count-1)){
					start=i*chunksize;
					end=i*chunksize+chunksize;
				}else{
					start=i*chunksize;
					end=i*chunksize+mod;
				}
				byte[] cutbytes=Arrays.copyOfRange(bytes, start, end);
				
				FileChunk fc=new FileChunk();
				fc.setChunkname(filename);
				fc.setFilemd5(filemd5);
				fc.setChunknumber(i);
				fc.setChunksize(cutbytes.length);
				fc.setTotalchunks(count);
				fc.setTotalsize(len);
				fc.setBytes(cutbytes);
				chunks.add(fc);
			}
		}else{
			FileChunk fc=new FileChunk();
			fc.setChunkname(filename);
			fc.setFilemd5(filemd5);
			fc.setChunknumber(0);
			fc.setChunksize(len);
			fc.setTotalchunks(1);
			fc.setTotalsize(len);
			fc.setBytes(bytes);
			chunks.add(fc);
		}
		
		return chunks;
	}
}

package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;
import com.micro.config.RedisChunkTemp;
import com.micro.model.DiskFile;

import lombok.Data;

@Data
public class MergeRequest extends ContextRequest{
	private String userid; //用户id
	private String username;
	private String pid; //文件pid（上传到哪个文件夹下）
	private String uuid; //前端上传uuid
	private String fileid; //前端上传的文件id
	private String filemd5; //文件md5
	private String filename; //文件名称
	private long totalSize; //文件大小
	private String albumid; //相册【*】
	private String relativepath; //文件夹上传（路径）【*】
	
	//后面补充的内容
	private List<RedisChunkTemp> temps=new ArrayList<>();
	private boolean existindiskmd5;//是否存在
	private boolean existindiskfile;//是否存在
	
	//如果存在disk_md5，则从里面取；否则从临时记录获取和裁剪获取
	private String typecode;
	private String Filesuffix;
	private String thumbnailurl;
	private String imgsize;
	
	private List<DiskFile> folders=new ArrayList<>();	
	private String diskfileid;
}

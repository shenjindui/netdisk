package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;
import com.micro.config.RedisChunkTemp;

import lombok.Data;

@Data
public class AppMergeRequest extends ContextRequest{
	private String appId;
	private String filemd5;
	private String filename;
	private long filesize;
	private String businessid;
	private String businesstype;
	private String userid;
	private String username;
	private Boolean secondUpload;
	private Boolean allowMultiple;
	
	//后面补充的内容
	private List<RedisChunkTemp> temps=new ArrayList<>();
	private boolean md5isExist;
	private String filesuffix;
	private String typecode;
}

package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;
import com.micro.modeltree.FileChunk;

import lombok.Data;

@Data
public class CreateRequest extends ContextRequest{
	private String pid;
	private String filename;
	private byte[] bytes;
	private String userid;
	private String username;
	
	//补充字段
	List<FileChunk> chunks=new ArrayList<>();
	private String filemd5;
	private boolean exist;
	private String fileid;
}

package com.micro.chain.param;

import com.micro.chain.core.ContextRequest;

import lombok.Data;

@Data
public class FolderRequest extends ContextRequest{
	private String pid;
	private String filename;
	private String userid;
	private String username;
	
	//补充字段
	private String diskfileid;
}

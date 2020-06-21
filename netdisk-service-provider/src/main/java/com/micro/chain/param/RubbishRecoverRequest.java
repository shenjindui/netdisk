package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;
import com.micro.modeltree.DiskFileDelTree;

import lombok.Data;

@Data
public class RubbishRecoverRequest extends ContextRequest{
	private String folderid;
	private List<String> ids=new ArrayList<>();
	private String userid;
	private String username;
	
	//补充
	private List<DiskFileDelTree> trees=new ArrayList<>();
	private long capacity;
	private List<String> rediskeys=new ArrayList<>();
	private List<String> solrids=new ArrayList<>();
}

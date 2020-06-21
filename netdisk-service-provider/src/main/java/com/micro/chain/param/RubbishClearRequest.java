package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;
import com.micro.modeltree.DiskFileDelTree;

import lombok.Data;

@Data
public class RubbishClearRequest extends ContextRequest{
	private List<String> ids=new ArrayList<>();
	private String userid;
	
	//补充
	private List<DiskFileDelTree> trees=new ArrayList<>();
	private List<String> rediskeys=new ArrayList<>();
}

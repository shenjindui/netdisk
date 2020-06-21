package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;
import com.micro.modeltree.DiskFileTree;

import lombok.Data;

@Data
public class CopyRequest extends ContextRequest{
	private String userid;
	private String username;
	private List<String> ids;
	private String folderid;
	
	//补充
	private List<DiskFileTree> files=new ArrayList<>();
	private long capacity;
	List<String> newFileIds=new ArrayList<>();
}

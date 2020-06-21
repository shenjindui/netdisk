package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;
import com.micro.modeltree.DiskShareFileTree;

import lombok.Data;

@Data
public class ShareSaveRequest extends ContextRequest{
	private String userid;
	private String username;
	private String folderid;
	private String shareid;
	private List<String> ids=new ArrayList<>();
	
	//补充
	private long capacity;
	private List<DiskShareFileTree> files=new ArrayList<>();
	private List<String> solrids=new ArrayList<>();
}

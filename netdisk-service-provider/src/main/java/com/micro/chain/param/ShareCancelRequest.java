package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;

import lombok.Data;

@Data
public class ShareCancelRequest extends ContextRequest{
	private List<String> ids=new ArrayList<>();
	
	//补充
	private List<String> secretIds=new ArrayList<>();
	private List<String> friendIds=new ArrayList<>();
}

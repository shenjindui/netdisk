package com.micro.chain.param;

import com.micro.chain.core.ContextRequest;
import com.micro.model.DiskShare;
import lombok.Data;

@Data
public class OverDueShareRequest extends ContextRequest{
	private String id;
	
	//补充
	private DiskShare diskShare=null;
}

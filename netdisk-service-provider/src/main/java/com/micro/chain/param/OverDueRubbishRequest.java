package com.micro.chain.param;

import com.micro.chain.core.ContextRequest;
import com.micro.model.DiskFileDel;

import lombok.Data;

@Data
public class OverDueRubbishRequest extends ContextRequest{
	private String id;
	//补充
	private DiskFileDel dfd=null;
}

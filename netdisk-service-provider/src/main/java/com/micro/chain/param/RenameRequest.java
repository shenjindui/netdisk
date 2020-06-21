package com.micro.chain.param;

import com.micro.chain.core.ContextRequest;

import lombok.Data;

@Data
public class RenameRequest extends ContextRequest{
	String userid;
	String id;
	String filename;
}

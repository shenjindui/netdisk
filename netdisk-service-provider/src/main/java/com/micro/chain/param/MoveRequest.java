package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;

import lombok.Data;

@Data
public class MoveRequest extends ContextRequest{
	private String userid;
	private List<String> ids=new ArrayList<>();
	private String folderid;
}

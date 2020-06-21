package com.micro.chain.param;

import java.util.List;

import com.micro.chain.core.ContextRequest;

import lombok.Data;

@Data
public class ShareSecretRequest extends ContextRequest{
	private List<String> ids;
	private String title;
	private String userid;
	private String username;
	private Integer sharetype;
	private Integer effect;
	private Integer type;
	
	//补充
	private String shareid;
	
}

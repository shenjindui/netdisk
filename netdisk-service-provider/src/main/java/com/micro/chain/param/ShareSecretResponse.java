package com.micro.chain.param;

import com.micro.chain.core.ContextResponse;

import lombok.Data;

@Data
public class ShareSecretResponse extends ContextResponse{
	private String url;
	private String code;
}

package com.micro.chain.param;

import com.micro.chain.core.ContextResponse;

import lombok.Data;

@Data
public class EditResponse extends ContextResponse {
	private String filename;
}

package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class ShareSecretResult implements Serializable{
	private String url;
	private String code;
}

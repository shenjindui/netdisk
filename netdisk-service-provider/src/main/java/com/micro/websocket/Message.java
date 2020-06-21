package com.micro.websocket;

import java.io.Serializable;

import lombok.Data;

@Data
public class Message implements Serializable{
	private Integer type;//0容量，1通知
	private Object data;
	private Integer count;
}

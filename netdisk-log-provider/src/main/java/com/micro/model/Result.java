package com.micro.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @author zwy
 * 响应实体类
 */
@Data
public class Result implements Serializable{
	private Integer code;//响应编码（0成功,1失败）
	private String msg;//响应信息（如：添加成功）
	private Object data;//
}

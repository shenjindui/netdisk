package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class TypeSuffixOperateBean implements Serializable{
	private String componentcode;//组件名称
	private String componentname;
}

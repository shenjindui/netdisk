package com.micro.disk.bean;

import java.io.Serializable;
import lombok.Data;


/**
 * 文件分类（比如：文档、图片、视频、音乐、其他）
 * @author Administrator
 *
 */
@Data
public class TypeBean implements Serializable{
	private String id;
	private String code;
	private String name;
}

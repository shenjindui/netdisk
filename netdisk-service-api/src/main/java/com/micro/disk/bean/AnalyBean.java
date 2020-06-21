package com.micro.disk.bean;

import java.io.Serializable;
import lombok.Data;

@Data
public class AnalyBean implements Serializable{
	private String totalFileNum;//文件数
	private String totalFileSize;//文件大小
	private String totalChunkNum;//切块数数

	private String userNum;
	private String userFileNum;
	private String userFileSize;
	
	private String searchMysqlNum;//
	private String searchSolrNum;//
	
	private String redisRubbishNum;//回收站过期监听
	private String redisShareNum;//分析过期监听
	
}

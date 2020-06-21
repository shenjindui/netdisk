package com.micro.modeltree;

import lombok.Data;

@Data
public class FileChunk {
	private String chunkname;
	private String filemd5;//文件的md5
	private Integer chunknumber;//第几块
	private long chunksize;//切块大小
	private Integer totalchunks;//总切块数
	private long totalsize;//总大小
	private byte[] bytes;
	
	private String storepath;
}

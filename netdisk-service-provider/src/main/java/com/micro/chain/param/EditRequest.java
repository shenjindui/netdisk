package com.micro.chain.param;

import java.util.ArrayList;
import java.util.List;

import com.micro.chain.core.ContextRequest;
import com.micro.modeltree.FileChunk;
import lombok.Data;

@Data
public class EditRequest extends ContextRequest {
	private String fileid;
	private byte[] bytes;
	
	//补充
	private List<FileChunk> chunks=new ArrayList<>();
	private String filemd5;
	private String prevfilemd5;
	private String filename;
	private String userid;
	private String username;
	private int type;//0（新增已用容量，减少总容量），1（减少已用容量，新增总容量）
	private long size;//
	private boolean exist;
}

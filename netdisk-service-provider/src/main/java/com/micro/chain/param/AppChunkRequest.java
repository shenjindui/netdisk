package com.micro.chain.param;

import java.util.Arrays;

import com.micro.chain.core.ContextRequest;

import lombok.Data;

@Data
public class AppChunkRequest extends ContextRequest{
	private byte[] bytes;
	private String appId;
	private String filemd5;
	private String filename;
	private String userid;
	
    private Long size; //文件大小
    private Integer chunk; //切块序号
    private Integer chunks; //切块数量
    
	//流转过程补充的字段
    private String storepath;
    private String typecode;
    
	@Override
	public String toString() {
		return "AppChunkRequest [appId=" + appId + ", filemd5=" + filemd5
				+ ", filename=" + filename + ", userid=" + userid + ", size=" + size + ", chunk=" + chunk + ", chunks="
				+ chunks + ", storepath=" + storepath + ", typecode=" + typecode + "]";
	}
    
    
}

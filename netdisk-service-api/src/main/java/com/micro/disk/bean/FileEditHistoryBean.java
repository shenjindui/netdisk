package com.micro.disk.bean;

import java.io.Serializable;
import lombok.Data;

@Data
public class FileEditHistoryBean implements Serializable{
	private String id;
	private String fileid;
	private String editusername;
	private String edittime;
	private String filemd5;
	private String prevfilemd5;
}

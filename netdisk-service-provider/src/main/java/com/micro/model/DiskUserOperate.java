package com.micro.model;

import java.util.Date;

import lombok.Data;

/**
 * 用户操作【网盘对应的-最近菜单】
 * 
 * @author Administrator
 *
 */
@Data
public class DiskUserOperate {
	private String id;
	private String userid;
	private Date createtime;
	
	//文件夹、相册---不在这里记录；删除了文件，则这里就变为空了
	//创建（上传、复制、转存、创建文件夹）
	//编辑（删除、重命名、移动）
	//分享
	//查看（下载、播放、打开、预览）
	private String type;
	private String typename;
	
	//关联文件
	private String fileid;
	private String filename;
	private String filemd5;
	private String filesize;
	private String typecode;
	private String filesuffix;
	private String fileicon;
}

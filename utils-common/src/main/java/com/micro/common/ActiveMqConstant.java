package com.micro.common;

public class ActiveMqConstant {
	public final static String FILE_UPLOAD="disk.fileupload.queue";//文件上传（合并）
	public final static String FILE_UPLOADFOLDER="disk.fileuploadfolder.queue";//文件夹上传（合并）
	public final static String FILE_DELETE="disk.filedelete.queue";//文件删除
	public final static String FILE_CREATE="disk.filecreate.queue";//文件创建
	public final static String FILE_EDIT="disk.fileedit.queue";//文件修改
	public final static String FOLDER_CREATE="disk.foldercreate.queue";//创建文件夹
	public final static String FILE_RENAME="disk.filerename.queue";//重命名
	public final static String FILE_COPYTO="disk.filecopy.queue";//复制
	public final static String FILE_MOVETO="disk.filemove.queue";//移动
	public final static String FILE_DOWNLOAD="disk.filedownload.queue";//文件下载
	public final static String FILE_OPEN="disk.fileopen.queue";//文件在线打开
	
	public final static String FILE_SHARE="disk.fileshare.queue";//分享
	public final static String FILE_SHARE_CANCEL="disk.filesharecancel.queue";//取消分享
	public final static String FILE_SHARE_SAVE="disk.sharesave.queue";//分享保存
	
	
	public final static String RUBBISH_RECOVER="disk.rubbishrecover.queue";//回收站还原
	public final static String RUBBISH_CLEAR="disk.rubbishclear.queue";//回收站清空
	
	public final static String NOTICE_READ="disk.noticeread.queue";//通知已读
	public final static String NOTICE_DELETE="disk.noticedelete.queue";//通知删除
	
	public final static String LOGS_CONTROLLER="disk.logs.controller.queue";//日志
	public final static String LOGS_SERVICE="disk.logs.service.queue";//日志
}

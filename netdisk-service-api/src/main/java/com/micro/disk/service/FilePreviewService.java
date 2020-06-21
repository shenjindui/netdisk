package com.micro.disk.service;

import java.util.List;
import java.util.Map;

import com.micro.disk.bean.DownloadBean;

public interface FilePreviewService {
	/**
	 * 缩略图
	 * @param url
	 * @return
	 */
	public byte[] getBytesByUrl(String url);
	
	/**
	 * 根据fileid下载
	 * @param fileid
	 * @return
	 */
	public List<String> getChunksByFileid(String fileid);
	/**
	 * 根据fileid下载
	 * @param fileid
	 * @return
	 */
	public List<String> getChunksByAppFileid(String fileid);
	/**
	 * 根据md5下载
	 * @param filemd5
	 * @return
	 */
	public List<String> getChunksByFilemd5(String filemd5);
	/**
	 * 获取文件信息
	 * @param userid
	 * @param fileids
	 * @return
	 */
	public DownloadBean getDownloadInfo(String userid,List<String> fileids);
	
}

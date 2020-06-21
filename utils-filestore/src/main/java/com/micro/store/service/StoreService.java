package com.micro.store.service;

public interface StoreService {
	/**
	 * 上传
	 * @param group组
	 * @param bytes
	 * @param fileName
	 */
	public String upload(String group,byte[] bytes,String fileName);
	/**
	 * 下载
	 * @param group
	 * @param path
	 * @return
	 */
	public byte[] download(String group,String path);
	/**
	 * 下载
	 * @param path
	 * @return
	 */
	public byte[] download(String path);
	/**
	 * 删除
	 * @param path
	 */
	public void delete(String path);
	/**
	 * 创建文件夹
	 * @param folders
	 */
	public void mkdir(String folders);
	/**
	 * 获取列表【废弃】
	 * @param folder
	 */
	public void listFiles(String folder);
}

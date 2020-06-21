package com.micro.office.preview;

public interface PreviewService {
	/**
	 * 转换pdf
	 * @param suffix
	 * @param bytes
	 * @return
	 */
	public byte[] converToPdf(String filename,byte[] bytes);
	
	/**
	 * 转换html
	 * @param bytes
	 * @return
	 */
	public byte[] converToHtml(byte[] bytes);
	
	/**
	 * 转换图片
	 * @param bytes
	 * @return
	 */
	public byte[] converToPng(byte[] bytes);
	
}

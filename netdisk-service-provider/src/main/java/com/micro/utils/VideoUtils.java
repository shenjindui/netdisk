package com.micro.utils;

import java.io.InputStream;
import java.util.List;

/**
 * 视频操作 技术：ffffmpeg
 * 
 * @author Administrator
 *
 */
public class VideoUtils {
	public static void main(String[] args) {
		try {
			String videoRealPath = "E:/apk/01.Netty实现高性能分布式RPC架构介绍.avi";
			// 截图的路径（输出路径）
			String imageRealPath = "E:/apk/zwy.jpg";

			List<String> commend = new java.util.ArrayList<String>();
			commend.add("D:\\mm\\ffmpeg.exe");
			commend.add("-i");
			commend.add(videoRealPath);
			commend.add("-y");
			commend.add("-f");
			commend.add("image2");
			commend.add("-ss");
			commend.add("1");
			commend.add("-t");
			commend.add("0.001");
			commend.add("-s");
			commend.add("350*240");
			commend.add(imageRealPath);
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.redirectErrorStream(true);
			System.out.println("视频截图开始...");
			Process process = builder.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			System.out.print("正在进行截图，请稍候");
			while (in.read(re) != -1) {
				System.out.print(".");
			}
			System.out.println("");
			in.close();
			System.out.println("视频截图完成...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("视频截图失败！");
		}
	}
}

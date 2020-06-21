package com.micro.netdisk.javasdk.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * 大文件切块
 * @author Administrator
 *
 */
public class FileCutUtils {
	private static long chunksize=1*1024*1024;

	/**
	 * 测试目标
	 * 第一，加载整个文件会内存溢出
	 * 第二，分别读取切块，则不会内存溢出
	 * 第三，多个人同时读取切块，不会内存溢出
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		File file=new File("E:/maven_repository.zip");
		FileInputStream fis=new FileInputStream(file);
		long filesize=file.length();
		System.out.println("文件大小："+file.length());
		//计算切块数量
		long chunkNum=0;//切块数
		long mod=0;//最后一块的真实大小
		if(filesize<chunksize){
			chunkNum=1;
			mod=filesize;
		}else{
			chunkNum=(filesize%chunksize)==0?(filesize/chunksize):(filesize/chunksize)+1;
			mod=filesize%chunksize;
		}
		System.out.println("切块数量："+chunkNum);
		//计算每个切块的下标
		for(long i=1;i<=chunkNum;i++){
			long start=0;
			long end=0;
			
			long len=0;
			if(i<chunkNum){
				start=(i-1)*chunksize;
				end=(i-1)*chunksize+chunksize;
				len=chunksize;
			}else{
				//最后一块切块可能不足chunksize，需要特殊处理
				start=(i-1)*chunksize;
				end=(i-1)*chunksize+mod;
				len=mod;
			}
			//读取File的字节流
			//byte[] cutbytes=Arrays.copyOfRange(bytes, start, end);
			byte[] cutBytes=new byte[(int) chunksize];
			fis.read(cutBytes, (int)start,(int)len);
			System.out.println("开始：==================================================================================================");
			System.out.println("输出切块:"+i+"，长度："+cutBytes.length);
			System.out.println("接受：==================================================================================================");
			cutBytes=null;
		}
		fis.close();
	}
	
	@Test
	public void testCut()throws Exception{
		File file=new File("H:/老婆.rar");
		FileInputStream fis=new FileInputStream(file);
		byte[] bytes=new byte[(int)chunksize];
		int len=0;
		int chunkNum=0;
		ExecutorService es=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		while((len=fis.read(bytes))>0){
			final int numThread=chunkNum;
			final int lenThread=len;
			es.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("切块："+numThread+"..........大小："+lenThread);
				}
			});
			chunkNum++;
		}
		fis.close();
	}
}

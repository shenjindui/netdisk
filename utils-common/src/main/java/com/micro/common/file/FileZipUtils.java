package com.micro.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZipUtils {
	public static void fileToZip(String sourceFilePath,String zipPath){
	    try {
	    	File zipFile = new File(zipPath) ;
	        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
	        recursionZip(sourceFilePath,"",zipOut);
	        zipOut.close() ;
	        delFile(sourceFilePath);
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
	}
	////////////////////////////////////////////【删除文件】///////////////////////////////////////////////
	private static void delFile(String path){
		File file=new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f=files[i];
			if(f.isFile()){
				f.delete();
			}else{
				dgDel(f);
			}
		}
		file.delete();
	}
	private static void dgDel(File file){
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f=files[i];
			if(f.isFile()){
				f.delete();
			}else{
				dgDel(f);
			}
		}
		file.delete();
	}
	////////////////////////////////////////////【压缩文件】///////////////////////////////////////////////
	
	public static void recursionZip(String filePath,String baseDir,ZipOutputStream zipOut) throws Exception{
        File srcFile = new File(filePath);
        File[] files = srcFile.listFiles();
        for(File file : files){
            if(file.isFile()){
                zipFile(file,baseDir,zipOut);
            }else{
                recursionZip(filePath + File.separator +file.getName(),baseDir + file.getName()+ File.separator,zipOut);
            }
        }
    }
	
	 public static void zipFile(File file,String baseDir,ZipOutputStream zipOut) throws Exception{
        InputStream input = new FileInputStream(file) ;
       //压缩出文件带目录，需要加上基础路径
        zipOut.putNextEntry(new ZipEntry(baseDir + file.getName())) ;
        byte[] buffer = new byte[4096];
        int readByte = -1;
        while((readByte=input.read(buffer))!=-1){
            zipOut.write(buffer,0,readByte) ;
        }
        input.close() ;
        zipOut.closeEntry();
    }
}

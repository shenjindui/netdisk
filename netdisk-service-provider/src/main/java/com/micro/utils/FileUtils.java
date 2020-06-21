package com.micro.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class FileUtils {
	
	/**
	 * 创建临时目录
	 * @param uploadFolder
	 * @param identifier 文件身份表示
	 * @param userid
	 * @param filename 
	 * @param chunkNumber
	 * @return
	 */
	public static String generatePath(String uploadFolder, String identifier,String userid,String filename,Integer chunkNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder).append("/").append(identifier).append("/").append(userid).append("/").append("chunk");
        //判断uploadFolder/identifier 路径是否存在，不存在则创建
        if (!Files.isWritable(Paths.get(sb.toString()))) {
            try {
                Files.createDirectories(Paths.get(sb.toString()));
            } catch (IOException e) {
            	e.printStackTrace();
            	throw new RuntimeException(e.getMessage());
            }
        }

        return sb.append("/")
                .append(filename)
                .append("-")
                .append(chunkNumber).toString();
    }
	
	public static void merge1111(String targetFile, String folder) {
        try {
        	if (!Files.isWritable(Paths.get(targetFile))) {
                try {
                	int index=targetFile.lastIndexOf("/");
                	String targetFolder=targetFile.substring(0, index);
                	
                    Files.createDirectories(Paths.get(targetFolder));
                } catch (IOException e) {
                	e.printStackTrace();
                	throw new RuntimeException(e.getMessage());
                }
            }
        	//扫描指定文件夹下的切块，排序，合并
            Files.createFile(Paths.get(targetFile));
            Files.list(Paths.get(folder))
                    .filter(path -> path.getFileName().toString().contains("-"))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            //Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

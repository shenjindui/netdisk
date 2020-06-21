package com.micro.chain.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.config.RedisChunkTemp;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.utils.ImageUtils;

@Component(value="picture")
public class FiledealImgStrategy implements FiledealStrategy{
	@Autowired
	private ImageUtils imageUtils;
	@Autowired
	private DiskMd5Dao diskMd5Dao;
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void deal(String fileid,String filemd5, List<RedisChunkTemp> temps) {
		//获取图片字节流
		byte[] bytes=imageUtils.getTotalbytes(temps);
		
		//获取宽高
		String imgsize=imageUtils.getImgWithAndHeight(bytes);
		
		//压缩图片
		String thumbnailurl=imageUtils.compressImg(bytes);
		
		//更新disk_file字段
		diskFileDao.updateField(thumbnailurl, imgsize, fileid);
		
		//更新disk_md5字段
		diskMd5Dao.updateField(thumbnailurl, imgsize, filemd5);
	}

	@Override
	public void deal(String filemd5, List<RedisChunkTemp> temps) {
		//获取图片字节流
		byte[] bytes=imageUtils.getTotalbytes(temps);
		
		//获取宽高
		String imgsize=imageUtils.getImgWithAndHeight(bytes);
		
		//压缩图片
		String thumbnailurl=imageUtils.compressImg(bytes);
		
		//更新disk_md5字段
		diskMd5Dao.updateField(thumbnailurl, imgsize, filemd5);		
	}
}

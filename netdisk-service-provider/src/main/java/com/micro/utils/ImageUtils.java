package com.micro.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.config.RedisChunkTemp;
import com.micro.store.context.StoreContext;
import com.micro.store.service.StoreService;

import net.coobird.thumbnailator.Thumbnails;

@Component
public class ImageUtils {
	@Autowired
	private StoreContext storeContext;
	
	//获取总字节流
	public byte[] getTotalbytes(List<RedisChunkTemp> temps){
		try {
			int current=0;
			int totallen=0;
			List<byte[]> arrs=new ArrayList<byte[]>();
			for(RedisChunkTemp temp:temps){
				byte[] bytes=storeContext.download(temp.getStorepath());
				arrs.add(bytes);
				totallen=totallen+bytes.length;
			}
			byte[] resultbytes=new byte[totallen];
			for(int i=0;i<arrs.size();i++){
				byte[] bytes=arrs.get(i);
				System.arraycopy(bytes, 0, resultbytes, current, bytes.length);
				current=current+bytes.length;
			}
			return resultbytes;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取字节流出错");
		}
	}
	//压缩图片
	public String compressImg(byte[] bytes){
		byte[] newbytes=thumbnail(bytes, 160, 160);
		return storeContext.upload("", newbytes,UUID.randomUUID()+"-compressimg"+".png");
	}
	//获取宽高
	public String getImgWithAndHeight(byte[] bytes){
		try {
			ByteArrayInputStream input=new ByteArrayInputStream(bytes);
			BufferedImage sourceImg = ImageIO.read(input);
			int width=sourceImg.getWidth();
			int height=sourceImg.getHeight();
			input.close();
			return width+"px*"+height+"px";
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("获取图片尺寸出错");
		}
	}
	
	//获取缩略图
	public byte[] thumbnail(byte[] bytes,int width,int height){
		try{			
			ByteArrayInputStream input=new ByteArrayInputStream(bytes);
			ByteArrayOutputStream output=new ByteArrayOutputStream();
			Thumbnails.of(input).size(width, height).outputFormat("jpg").toOutputStream(output);
			byte[] b= output.toByteArray();
			input.close();
			output.close();
			return b;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
}

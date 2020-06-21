package com.micro.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.ValidateUtils;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.TypeSuffixBean;
import com.micro.disk.service.TypeService;
import com.micro.disk.service.TypeSuffixService;

@RestController
@RequestMapping("/typesuffix")
public class TypeSuffixController {
	@Reference(check=false)
	private TypeService typeService;
	@Reference(check=false)
	private TypeSuffixService typeSuffixService;
	
	/**
	 * 格式列表（分页）
	 * @param pi
	 * @param typecode
	 * @return
	 */
	@RequestMapping("/findList")
	public PageInfo<TypeSuffixBean> findTypeSuffix(PageInfo<TypeSuffixBean> pi,String typecode){
		return typeSuffixService.findList(pi.getPage(), pi.getLimit(), typecode);
	}
	
	/**
	 * 查询单条记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Result findTypeSuffixOne(String id){
		try{
			return ResultUtils.success("查询成功", typeSuffixService.findOne(id));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 根据格式查询其组件列表
	 * @param suffix
	 * @return
	 */
	@RequestMapping("/findOperateBySuffix")
	public Result findOperateBySuffix(String suffix){
		try{
			
			return ResultUtils.success("查询成功", typeSuffixService.findComponentsBySuffix(suffix));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 保存
	 * @param typecode
	 * @param name
	 * @param suffix
	 * @param json
	 * @param file
	 * @param filebig
	 * @return
	 */
	@RequestMapping("/save")
	public Result save(String typecode,String name,String suffix,String json,MultipartFile file,MultipartFile filebig){
		try{
			String icon="";
			String iconbig="";
			//32*32图标
			if(file!=null){
				icon=getIconStr(file.getOriginalFilename(), file.getBytes(),32);
			}
			//128*128图标
			if(filebig!=null){
				icon=getIconStr(filebig.getOriginalFilename(), filebig.getBytes(),128);
			}
			
			//组件集合
			List<String> operatecodes=new ArrayList<String>();
			if(!StringUtils.isEmpty(json)){
				String[] arrs=json.split(",");
				for(String code:arrs){
					operatecodes.add(code.toString());
				}
			}
			typeSuffixService.save(typecode, name, icon, iconbig, suffix, operatecodes);
			return ResultUtils.success("添加成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	private String getIconStr(String filename,byte[] bytes,int size) throws IOException{
		//控制尺寸
		if(!filename.endsWith(".png")){
			throw new RuntimeException("图标必须是.png格式");
		}
		InputStream input=new ByteArrayInputStream(bytes);
		BufferedImage sourceImg=ImageIO.read(input);
		int width=sourceImg.getWidth(); //获取图标的宽
		int height=sourceImg.getHeight(); //获取图标的高
		
		if(width!=size||height!=size){
			throw new RuntimeException("图标尺寸必须是"+size+"*"+size);
		}
		input.close();
		
		//转换base64
		String icon=Base64.encodeBase64String(bytes);
		return icon;
	}
	
	/**
	 * 更新
	 * @param id
	 * @param name
	 * @param suffix
	 * @param json
	 * @param file
	 * @param filebig
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(String id,String name,String suffix,String json,MultipartFile file,MultipartFile filebig){
		try{
			String icon="";
			String iconbig="";
			//32*32图标
			if(file!=null){
				icon=getIconStr(file.getOriginalFilename(), file.getBytes(),32);
			}
			//128*128图标
			if(filebig!=null){
				icon=getIconStr(filebig.getOriginalFilename(), filebig.getBytes(),128);
			}
			//组件集合
			List<String> operatecodes=new ArrayList<String>();
			if(!StringUtils.isEmpty(json)){
				String[] arrs=json.split(",");
				for(String code:arrs){
					operatecodes.add(code.toString());
				}
			}
			typeSuffixService.update(id, name, icon, iconbig, suffix, operatecodes);
			return ResultUtils.success("修改成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	/**
	 * 删除记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(String id){
		try{
			typeSuffixService.delete(id);
			return ResultUtils.success("删除成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

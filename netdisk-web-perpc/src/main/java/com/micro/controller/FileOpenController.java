package com.micro.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.MimeUtils;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.file.ByteResponse;
import com.micro.disk.bean.FileBean;
import com.micro.disk.service.FilePreviewService;
import com.micro.disk.service.FileService;
import com.micro.disk.service.TypeSuffixService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;
import com.micro.office.preview.PreviewContext;
import com.micro.office.preview.PreviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="文件在线预览")
@RestController
@RequestMapping("/disk/fileopen")
public class FileOpenController {
	@Reference(check=false)
	private FilePreviewService filePreviewService;
	
	@Reference(check = false)
	private FileService fileService;
	
	@Reference(check=false)
	private TypeSuffixService typeSuffixService;
	
	@Autowired
	private PreviewContext previewContext;
	
	@ApiOperation(value="根据后缀查询关联组件",notes="根据后缀查询关联组件")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "filesuffix", value = "文件后缀（如：doc）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findComponentsBySuffix")
	public Result findComponentsBySuffix(String filesuffix){
		try{
			return ResultUtils.success("查询成功", typeSuffixService.findComponentsBySuffix(filesuffix));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="根据thumbnailurl展示缩略图",notes="用于：图标缩略图展示")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "url", value = "缩略图地址",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@GetMapping("/previewThumbnail")
	public void previewThumbnail(String url,HttpServletRequest request,HttpServletResponse response){
		try{
			byte[] bytes=filePreviewService.getBytesByUrl(url);
			ByteResponse.writeImage(request, response, bytes);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@ApiOperation(value="返回字节流",notes="用于：图片、音频、视频播放、pdf、txt、java等")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "fileid", value = "文件ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "code", value = "编码",dataType = "String",paramType="query",required=false),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@GetMapping("/writeByte")
	public void writeByte(String fileid,String code,HttpServletRequest request,HttpServletResponse response){
		try{			
			FileBean fb=fileService.findOne(fileid);
			if(fb==null){
				throw new RuntimeException("文件ID不存在");
			}
			String filemd5=fb.getFilemd5();
			String filename=fb.getFilename();
			SessionUserBean user=UserInfoUtils.getBean(request);
			if(!user.getId().equals(fb.getCreateuserid())){
				throw new RuntimeException("您不是文件的拥有者,无法获取文件");
			}
			
			if(StringUtils.isEmpty(code)){
				code="UTF-8";
			}
			String mime=MimeUtils.getMime(fb.getFilesuffix());
			response.setHeader("Content-Type", ""+mime+";charset="+code);
			response.setHeader("Content-Disposition", "inline;filename="+filename);
			
			//Office文件对应的格式
			String[] types={"doc","docx","xls","xlsx","ppt","pptx"};
			boolean isoffice=false;
			for(String type:types){
				if(type.equals(fb.getFilesuffix())){
					isoffice=true;
					break;
				}
			}
			if(isoffice){
				officeFile(filemd5, filename, response);
			}else{				
				commonFile(filemd5,response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void commonFile(String filemd5,HttpServletResponse response) throws IOException{
		List<String> urls=filePreviewService.getChunksByFilemd5(filemd5);
		OutputStream out=response.getOutputStream();
		for(String url:urls){				
			byte[] bytes=filePreviewService.getBytesByUrl(url);
			out.write(bytes);
			out.flush();
		}
		out.close();
	}
	private void officeFile(String filemd5,String filename,HttpServletResponse response){
		File file=null;
		try{
			//1.获取切块信息
			List<String> urls=filePreviewService.getChunksByFilemd5(filemd5);
			
			//2.把切块写到临时目录合并成完整的Office文件
			List<byte[]> arrs=new ArrayList<byte[]>();
			int current=0;
			int totallen=0;
			for(String url:urls){				
				byte[] bytes=filePreviewService.getBytesByUrl(url);
				arrs.add(bytes);
				totallen=totallen+bytes.length;
			}
			byte[] resultbytes=new byte[totallen];
			for(int i=0;i<arrs.size();i++){
				byte[] bytes=arrs.get(i);
				System.arraycopy(bytes, 0, resultbytes, current, bytes.length);
				current=current+bytes.length;
			}
			
			//3.获取PDF字节数组
			byte[] pdfbytes=previewContext.converToPdf(resultbytes);
			//resultbytes=null;//清空
			
			//4.写回字节数组
			OutputStream out=response.getOutputStream();
	        out.write(pdfbytes);
	        out.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(file!=null){				
				file.delete();			
			}
		}
	}
	
	@ApiOperation(value="在线编辑-内容显示",notes="在线编辑-内容显示")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "fileid", value = "文件ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "code", value = "编码",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/writeTextStr")
	public Result writeTextStr(String fileid,String code,HttpServletRequest request,HttpServletResponse response){
		try{			
			FileBean fb=fileService.findOne(fileid);
			if(fb==null){
				throw new RuntimeException("文件ID不存在");
			}
			String filemd5=fb.getFilemd5();
			SessionUserBean user=UserInfoUtils.getBean(request);
			if(!user.getId().equals(fb.getCreateuserid())){
				throw new RuntimeException("您不是文件的拥有者,无法获取文件");
			}
			String data=getText(filemd5, code);
			return ResultUtils.success("查询成功", data);
		}catch(Exception e){
			e.printStackTrace();
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="文件对比-内容显示",notes="文件对比-内容显示")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "filemd5", value = "文件MD5",dataType = "String",paramType="query",required=true),
		@ApiImplicitParam(name = "code", value = "编码",dataType = "String",paramType="query",required=true),
		@ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
	})
	@PostMapping("/writeTextStrByMd5")
	public Result writeTextStrByMd5(String filemd5,String code,HttpServletRequest request,HttpServletResponse response){
		try{			
			String data=getText(filemd5, code);
			return ResultUtils.success("查询成功", data);
		}catch(Exception e){
			e.printStackTrace();
			return ResultUtils.error(e.getMessage());
		}
	}
	private String getText(String filemd5,String code) throws UnsupportedEncodingException{
		List<String> urls=filePreviewService.getChunksByFilemd5(filemd5);
		List<byte[]> arrs=new ArrayList<byte[]>();
		int current=0;
		int totallen=0;
		for(String url:urls){				
			byte[] bytes=filePreviewService.getBytesByUrl(url);
			arrs.add(bytes);
			totallen=totallen+bytes.length;
		}
		byte[] resultbytes=new byte[totallen];
		for(int i=0;i<arrs.size();i++){
			byte[] bytes=arrs.get(i);
			System.arraycopy(bytes, 0, resultbytes, current, bytes.length);
			current=current+bytes.length;
		}
		if(StringUtils.isEmpty(code)){
			code="UTF-8";
		}
		String data=new String(resultbytes, code);
		return data;
	}
}

package com.micro.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.disk.bean.Md5Bean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.FilePreviewService;
import com.micro.disk.service.Md5Service;

@RestController
@RequestMapping("/file")
public class FileController {
	@Reference(check=false)
	private Md5Service md5Service;
	
	@Reference(check = false)
	private FilePreviewService filePreviewService;
	
	@RequestMapping("/findList")
	public PageInfo<Md5Bean> findList(PageInfo<Md5Bean> pi,String filename,String md5){
		return md5Service.findList(pi.getPage(), pi.getLimit(), filename,md5);
	}
	
	@RequestMapping("/download")
	public void download(String filename,String md5,HttpServletRequest request, HttpServletResponse response){
		try {
			List<String> urls = filePreviewService.getChunksByFilemd5(md5);
			String userAgent = request.getHeader("User-Agent");
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {// 针对IE或者以IE为内核的浏览器：
				filename = java.net.URLEncoder.encode(filename, "UTF-8");
			} else {// 非IE浏览器的处理：
				filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setHeader("content-disposition", "attachment;filename=" + filename);
			// response.setContentType("application/octet-stream");
			OutputStream out = response.getOutputStream();

			for (String url : urls) {
				byte[] bytes = filePreviewService.getBytesByUrl(url);
				InputStream is = new ByteArrayInputStream(bytes);// byte[]转inputstream
				byte[] bs = new byte[1024];
				int len;
				while ((len = is.read(bs)) != -1) {
					out.write(bs, 0, len);
					out.flush();
				}
				is.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

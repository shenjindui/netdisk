package com.micro.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.common.CapacityUtils;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.ValidateUtils;
import com.micro.common.file.FileZipUtils;
import com.micro.disk.bean.DownloadBean;
import com.micro.disk.bean.FileBean;
import com.micro.disk.service.FilePreviewService;
import com.micro.disk.service.FileService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="文件下载")
@RestController
@RequestMapping("/disk/filedownload")
public class FileDownloadController {
	@Reference(check = false)
	private FilePreviewService filePreviewService;
	@Reference(check = false)
	private FileService fileService;
	
	@Value("${spring.application.name}")
	private String appname;

	@NacosValue(value="${download.temp.path}",autoRefreshed=true)
	private String downloadpath;
	
	@NacosValue(value="${download.temp.host}",autoRefreshed=true)
	private String downloadhost;
	
	@ApiOperation(value="单文件下载",notes="单文件下载")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "fileid", value = "文件ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@GetMapping("/download")
	public void download(String fileid, HttpServletRequest request, HttpServletResponse response) {
		try {
			//1.根据文件ID查询单条记录，并且做基本校验
			FileBean fb=fileService.findOne(fileid);
			if(fb==null){
				throw new RuntimeException("文件ID不存在");
			}
			String filemd5=fb.getFilemd5();
			String filename=fb.getFilename();
			SessionUserBean user=UserInfoUtils.getBean(request);
			if(!user.getId().equals(fb.getCreateuserid())){
				throw new RuntimeException("您不是文件的拥有者,无法下载");
			}
			
			//2.设置响应头
			String userAgent = request.getHeader("User-Agent");
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {// 针对IE或者以IE为内核的浏览器：
				filename = java.net.URLEncoder.encode(filename, "UTF-8");
			} else {// 非IE浏览器的处理：
				filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setHeader("content-disposition", "attachment;filename=" + filename);
			// response.setContentType("application/octet-stream");
			OutputStream out = response.getOutputStream();

			//3.根据文件MD5查询切块存储路径集合
			List<String> urls = filePreviewService.getChunksByFilemd5(filemd5);
			for (String url : urls) {
				//2.1、根据存储路径查询字节流
				byte[] bytes = filePreviewService.getBytesByUrl(url);
				
				//2.2、返回字节流给浏览器
				out.write(bytes);
				out.flush();
			}
			out.close();
			
			//核心：所有的切块公用一个输出流OutputStream out = response.getOutputStream();，并且按顺序返回，前端浏览器自动合并切块
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ApiOperation(value="多文件下载-获取文件信息",notes="是否大于200M（超出200M则使用客户端下载）")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "idjson", value = "勾选文件ID（[{'id':'xx'}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/getDownloadInfo")
	public Result getDownloadInfo(String idjson,HttpServletRequest request) {
		try {
			ValidateUtils.validate(idjson, "下载记录");

			List<String> fileids = new ArrayList<String>();
			String[] ids = idjson.split(",");
			for (String id : ids) {
				fileids.add(id);
			}
			SessionUserBean user=UserInfoUtils.getBean(request);
			DownloadBean bean = filePreviewService.getDownloadInfo(user.getId(),fileids);
			bean.setTotalsizename(CapacityUtils.convert(bean.getTotalsize()));
			if (bean.getTotalsize() <= 200 * 1024 * 2014) {
				bean.setIsbig(0);
			} else {
				bean.setIsbig(1);
			}
			return ResultUtils.success("查询成功", bean);
		} catch (Exception e) {
			return ResultUtils.error(e.getMessage());
		}
	}

	// 思路：
	// 1、下载前先获取所有下载文件的信息（总大小、总文件数量、总文件夹数量）
	// 2、如果大于200M则提示使用客户端下载
	// 3、如果小于200M
	// 3.1：在服务器本地生成临时目录，并获取所有切块到临时目录
	// 3.2：排序切块、合并切块
	// 3.3：压缩文件夹
	// 3.4：返回浏览器
	// 3.5：删除临时目录
	@ApiOperation(value="多文件下载-合并切块，压缩文件",notes="1：选择多个文件同时下载；2：选择某个文件夹，底下n个目录，n个文件")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "downloadname", value = "压缩后的名称",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "downloadsuffix", value = "压缩后的格式",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "idjson", value = "勾选文件ID（[{'id':'xx'}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/mergeFiles")
	public Result mergeFiles(String downloadname, String downloadsuffix, String idjson, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// 1、校验
			ValidateUtils.validate(downloadname, "下载文件名称");
			ValidateUtils.validate(downloadsuffix, "下载文件格式");
			ValidateUtils.validate(idjson, "下载记录");
			String[] fileids = idjson.split(",");

			// 2、生成临时文件夹
			SessionUserBean user = UserInfoUtils.getBean(request);
			String path = downloadpath + "/" + user.getId() + "/" + downloadname;

			File fileRootZip = new File(path + "." + downloadsuffix);
			if (fileRootZip.exists()) {
				throw new RuntimeException("该下载名称已经存在,请更换一个!");
			}

			File fileRoot = new File(path);
			if (!fileRoot.exists()) {
				fileRoot.mkdirs();
			} else {
				throw new RuntimeException("该下载名称已经存在,请更换一个!");
			}
			// 3、下载切块并合并文件
			for (String fileid : fileids) {
				FileBean bean = fileService.findOne(fileid);
				if (bean.getFiletype() == 0) {
					File file = new File(path + "/" + bean.getFilename());
					// 生成文件夹
					if (!file.exists()) {
						file.mkdirs();
					}
					// 递归
					dgDownload(user.getId(),path + "/" + bean.getFilename(), bean.getId());
				} else {
					String filename = path + "/" + bean.getFilename();
					FileOutputStream out = new FileOutputStream(filename);
					
					//根据文件MD5查询切块存储路径集合
					List<String> urls = filePreviewService.getChunksByFilemd5(bean.getFilemd5());
					for (String url : urls) {
						//根据每一个路径获取相应的字节流
						byte[] bytes = filePreviewService.getBytesByUrl(url);
						
						//往临时目录输出文件，自动合并切块
						out.write(bytes);
						out.flush();
					}
					out.close();
				}
			}
			// 4、压缩文件
			String zipPath = downloadpath + "/" + user.getId() + "/" + downloadname + "."+ downloadsuffix;
			FileZipUtils.fileToZip(path, zipPath);

			// 5、返回：为什么需要把ip:port返回，因为集群模式，合并请求和下载请求未必是同一台服务器处理的
			//InetAddress addr = InetAddress.getLocalHost();
			String filename = downloadname + "." + downloadsuffix;
			String paths = path + "." + downloadsuffix;
			//String ip=IpUtils.getInternetIp();
			//返回具体的路径，让前端直接直连下载
			String url = "http://" + downloadhost + "/"+appname+"/disk/filedownload/downloadZip?filename=" + filename + "&path=" + paths;

			return ResultUtils.success("压缩成功", url);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error(e.getMessage());
		}
	}
	private void dgDownload(String userid,String path, String pid) throws Exception {
		List<FileBean> beans = fileService.findChildrenFiles(userid,pid);
		if (!CollectionUtils.isEmpty(beans)) {
			for (FileBean bean : beans) {
				if (bean.getFiletype() == 0) {
					File file = new File(path + "/" + bean.getFilename());
					// 生成文件夹
					if (!file.exists()) {
						file.mkdirs();
					}
					// 递归
					dgDownload(userid,path + "/" + bean.getFilename(), bean.getId());
				} else {
					String filename = path + "/" + bean.getFilename();
					FileOutputStream out = new FileOutputStream(filename);
					List<String> urls = filePreviewService.getChunksByFilemd5(bean.getFilemd5());
					for (String url : urls) {
						byte[] bytes = filePreviewService.getBytesByUrl(url);
						out.write(bytes);
						out.flush();
					}
					out.close();
				}
			}
		}
	}

	@ApiOperation(value="多文件下载-压缩文件下载",notes="多文件下载-压缩文件下载")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "filename", value = "压缩后的名称",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "path", value = "压缩文件存储路径",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@GetMapping("/downloadZip")
	public void downloadZip(String filename, String path, HttpServletRequest request, HttpServletResponse response) {
		try {
			String userAgent = request.getHeader("User-Agent");
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {// 针对IE或者以IE为内核的浏览器：
				filename = java.net.URLEncoder.encode(filename, "UTF-8");
			} else {// 非IE浏览器的处理：
				filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setHeader("content-disposition", "attachment;filename=" + filename);
			// response.setContentType("application/octet-stream");
			OutputStream out = response.getOutputStream();

			File file = new File(path);
			FileInputStream input = new FileInputStream(file);
			byte[] bs = new byte[1024];
			int len;
			while ((len = input.read(bs)) != -1) {
				out.write(bs, 0, len);
				out.flush();
			}
			input.close();
			out.close();

			// 删除zip
			file.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

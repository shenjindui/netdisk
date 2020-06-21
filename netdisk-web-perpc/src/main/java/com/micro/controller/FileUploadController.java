package com.micro.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.Chunk;
import com.micro.disk.bean.MergeFileBean;
import com.micro.disk.service.FileService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import com.micro.mvc.UserInfoUtils;
import com.micro.pojo.ChunkPojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Api(tags="文件上传")
@RestController
@RequestMapping("/disk/fileupload")
public class FileUploadController {
	@Reference(check = false)
	private FileService fileService;
	@Reference(check = false,timeout=120000)
	private UserService userService;

	@ApiOperation(value="切块上传",notes="切块上传")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "file", value = "切块",dataType = "file",paramType="form",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/uploadChunk")
	public Result uploadChunk(MultipartFile file, ChunkPojo chunkPojo, String token, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			//1.通过token获取用户信息
			SessionUserBean user = userService.getUserByToken(token);
			if (user == null) {
				throw new RuntimeException("token无效");
			}else{
				request.setAttribute("userid", user.getId());
				request.setAttribute("username",user.getNickname());
			}
			//2.判断切块是否为空
			if (file == null) {
				throw new RuntimeException("切块不能为空");
			}
			
			//3.参数设置
			Chunk chunk=new Chunk();
			BeanUtils.copyProperties(chunkPojo, chunk);
			chunk.setUserid(user.getId());
			chunk.setUsername(user.getNickname());
			chunk.setBytes(file.getBytes());
			
			//4.调用切块上传接口
			fileService.uploadChunk(chunk);

			return ResultUtils.success("上传切块成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
			throw new RuntimeException(e.getMessage());
		}
	}

	@ApiOperation(value="检查文件是否存在",notes="检查文件是否存在")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "filemd5", value = "文件MD5",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "filesize", value = "文件大小",dataType = "Long",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/checkFile")
	public Result checkFile(String filemd5,long filesize) {
		try {
			return ResultUtils.success("查询成功", fileService.checkFile(filemd5));
		} catch (Exception e) {
			return ResultUtils.error(e.getMessage());
		}
	}

	@ApiOperation(value="检查切块是否存在",notes="检查切块是否存在")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "filemd5", value = "文件MD5",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "chunkNumber", value = "切块序号",dataType = "Integer",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@Deprecated
	@PostMapping("/checkChunk")
	public Result checkChunk(String filemd5, Integer chunkNumber, HttpServletRequest request) {
		try {
			SessionUserBean user = UserInfoUtils.getBean(request);
			return ResultUtils.success("查询成功", fileService.checkChunk(user.getId(), filemd5, chunkNumber));
		} catch (Exception e) {
			return ResultUtils.error(e.getMessage());
		}
	}

	@ApiOperation(value="切块合并",notes="切块合并")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "bean", value = "参数实体",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/mergeChunk")
	public Result mergeChunk(MergeFileBean bean, HttpServletRequest request) {
		try {
			SessionUserBean user = UserInfoUtils.getBean(request);
			bean.setUserid(user.getId());
			bean.setUsername(user.getNickname());
			fileService.mergeChunk(bean);
			return ResultUtils.success("合并切块成功", null);
		} catch (Exception e) {
			return ResultUtils.error(e.getMessage());
		}
	}
}

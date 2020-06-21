package com.micro.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.disk.bean.FileEditHistoryBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.ShareFriendsBean;
import com.micro.disk.service.FileEditHistoryService;
import com.micro.disk.service.FileService;
import com.micro.disk.service.ShareService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import com.micro.idempotence.NoRepeat;
import com.micro.mvc.UserInfoUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="文件通用操作")
@RestController
@RequestMapping("/disk/filecommon")
public class FileCommonController {
	@Reference(check=false)
	private FileService fileService;
	@Reference(check=false)
	private ShareService shareService;
	@Reference(check=false)
	private UserService userService;
	@Reference(check=false)
	private FileEditHistoryService fileEditHistroyService;
	
	@ApiOperation(value="创建文件夹",notes="创建文件夹")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "pid", value = "父节点ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "name", value = "文件夹名称",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/addFolder")
	public Result addFolder(String pid,String name,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			String userid=user.getId();
			String username=user.getNickname();
			
			fileService.addFolder(pid, name, userid, username);
			return ResultUtils.success("创建成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}

	@ApiOperation(value="查询单条记录",notes="查询单条记录")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "文件ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findOne")
	public Result findOne(String id){
		try{
			return ResultUtils.success("查询成功", fileService.findOne(id));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="文件夹属性",notes="文件夹属性")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "文件夹ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findFolderProp")
	public Result findFolderProp(String id,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			return ResultUtils.success("查询成功", fileService.findFolderProp(user.getId(),id));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="文件编辑历史版本",notes="文件编辑历史版本")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "fileid", value = "文件ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findEditHistory")
	public PageInfo<FileEditHistoryBean> findEditHistory(PageInfo<FileEditHistoryBean> pi,String fileid){
		return fileEditHistroyService.findList(pi.getPage(),pi.getLimit(),fileid);
	}
	
	@ApiOperation(value="重命名",notes="重命名")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "文件ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "filename", value = "文件名称",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/rename")
	public Result rename(String id,String filename,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			fileService.rename(user.getId(),id, filename);
			return ResultUtils.success("重命名成功", null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="删除文件",notes="删除文件")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "文件ID的json串（[{\"id\":\"xx\"}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/delete")
	public Result delete(String ids,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			if(StringUtils.isEmpty(ids)){
				throw new RuntimeException("请选择需要删除的记录!");
			}
			JsonUtils jsonUtils=new JsonJackUtils();
			List<Map> lists=(List<Map>) jsonUtils.jsonToList(ids, Map.class);
			
			List<String> fileids=new ArrayList<String>();
			for(Map m:lists){
				fileids.add(m.get("id").toString());
			}
			
			fileService.delete(user.getId(),user.getNickname(),fileids);
			return ResultUtils.success("删除成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="文件夹树",notes="文件夹树（懒加载），主要用于文件复制、移动等弹出框架选择目录")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "pid", value = "父节点ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "idjson", value = "勾选文件id的json串（[{\"id\":\"xx\"}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@Deprecated
	@PostMapping("/findFolderTree")
	public Result findFolderTree(HttpServletRequest request,String pid,String idjson){
		try{
			//json转换
			List<String> ids=new ArrayList<String>();
			if(!StringUtils.isEmpty(idjson)){				
				JsonUtils jsonUtils=new JsonJackUtils();
				List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
				lists.forEach(m->{
					ids.add(m.get("id")==null?"":m.get("id").toString());
				});
			}
			
			SessionUserBean user=UserInfoUtils.getBean(request);
			return ResultUtils.success("查询成功",fileService.findFolderTree(user.getId(), pid, ids));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="文件夹列表",notes="主要用于 复制、移动、上传--选择文件夹")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "pid", value = "父节点ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "idjson", value = "勾选文件id的json串（[{\"id\":\"xx\"}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findFolderList")
	public Result findFolderList(HttpServletRequest request,String pid,String idjson){
		try{
			//json转换
			List<String> ids=new ArrayList<String>();
			if(!StringUtils.isEmpty(idjson)){				
				JsonUtils jsonUtils=new JsonJackUtils();
				List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
				lists.forEach(m->{
					ids.add(m.get("id")==null?"":m.get("id").toString());
				});
			}
			
			SessionUserBean user=UserInfoUtils.getBean(request);
			return ResultUtils.success("查询成功",fileService.findFolderList(user.getId(), pid, ids));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="文件复制",notes="文件复制")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "idjson", value = "勾选文件id的json串（[{\"id\":\"xx\"}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "toid", value = "复制目标文件夹id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/copyTo")
	public Result copyTo(String idjson,String toid,HttpServletRequest request){
		try{
			SessionUserBean user=UserInfoUtils.getBean(request);
			String userid=user.getId();
			String username=user.getNickname();
			
			//json转换
			List<String> ids=new ArrayList<String>();
			if(!StringUtils.isEmpty(idjson)){				
				JsonUtils jsonUtils=new JsonJackUtils();
				List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
				lists.forEach(m->{
					ids.add(m.get("id")==null?"":m.get("id").toString());
				});
			}
			
			fileService.copyTo(userid,username,ids, toid);
			return ResultUtils.success("复制成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="文件移动",notes="文件移动")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "idjson", value = "勾选文件id的json串（[{\"id\":\"xx\"}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "toid", value = "移动目标文件夹id",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/moveTo")
	public Result moveTo(String idjson,String toid,HttpServletRequest request){
		try{
			//json转换
			List<String> ids=new ArrayList<String>();
			if(!StringUtils.isEmpty(idjson)){				
				JsonUtils jsonUtils=new JsonJackUtils();
				List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
				lists.forEach(m->{
					ids.add(m.get("id")==null?"":m.get("id").toString());
				});
			}
			SessionUserBean user=UserInfoUtils.getBean(request);
			fileService.moveTo(user.getId(),ids, toid);
			return ResultUtils.success("移动成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="查询某个节点的所有父节点",notes="主要用于查看文件的所有父目录")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "pid", value = "父节点ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findParentListByPid")
	public Result findParentListByPid(String pid){
		try{
			return ResultUtils.success("查询成功",fileService.findParentListByPid(pid));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="查询某个节点的所有父节点",notes="主要用于查看文件的所有父目录")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "文件ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findParentListById")
	public Result findParentListById(String id){
		try{
			return ResultUtils.success("查询成功",fileService.findParentListById(id));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="用户树",notes="用户树（懒加载）")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "pid", value = "父节点ID",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "type", value = "类型（org-机构，position-职务，user-用户）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/findUserTree")
	public Result findUserTree(String pid,String type){
		try{		
			return ResultUtils.success("查询成功", userService.findUserTree(pid, type));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="私密链接分享",notes="私密链接分享")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "idjson", value = "勾选文件id的json串（[{\"id\":\"xx\"}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "title", value = "分享标题",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "sharetype", value = "0有提取码，1无提取码",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "effect", value = "有效期（0永久，7表示7天，1表示1天）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "type", value = "0普通分享，1相册分享",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/shareSecret")
	public Result shareSecret(String idjson,String title,Integer sharetype,Integer effect,Integer type,HttpServletRequest request){
		try{
			//json转换
			List<String> ids=new ArrayList<String>();
			if(!StringUtils.isEmpty(idjson)){				
				JsonUtils jsonUtils=new JsonJackUtils();
				List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
				lists.forEach(m->{
					ids.add(m.get("id")==null?"":m.get("id").toString());
				});
			}
			
			SessionUserBean user=UserInfoUtils.getBean(request);
			String userid=user.getId();
			String username=user.getNickname();
			return ResultUtils.success("分享成功",shareService.shareSecret(ids,title, userid, username, sharetype, effect,type));
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
	
	@ApiOperation(value="好友分享",notes="好友分享")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "idjson", value = "勾选文件id的json串（[{\"id\":\"xx\"}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "title", value = "分享标题",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "userJson", value = "勾选用户json串（[{\"id\":\"xx\"}]）",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "type", value = "0普通分享，1相册分享",dataType = "String",paramType="query",required=true),
        @ApiImplicitParam(name = "token", value = "token",dataType = "String",paramType="query",required=true)
    })
	@PostMapping("/shareFriends")
	public Result shareFriends(String idjson,String title,String userJson,Integer type,HttpServletRequest request){
		try{
			//json转换
			List<String> ids=new ArrayList<String>();
			if(!StringUtils.isEmpty(idjson)){				
				JsonUtils jsonUtils=new JsonJackUtils();
				List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
				lists.forEach(m->{
					ids.add(m.get("id")==null?"":m.get("id").toString());
				});
			}
			
			List<ShareFriendsBean> friends=new ArrayList<>();
			if(!StringUtils.isEmpty(userJson)){
				JsonUtils jsonUtils=new JsonJackUtils();
				friends=jsonUtils.jsonToList(userJson, ShareFriendsBean.class);
			}
			
			SessionUserBean user=UserInfoUtils.getBean(request);
			String userid=user.getId();
			String username=user.getNickname();
			
			
			shareService.shareFriends(ids,friends,title, userid, username,type);
			return ResultUtils.success("分享成功",null);
		}catch(Exception e){
			return ResultUtils.error(e.getMessage());
		}
	}
}

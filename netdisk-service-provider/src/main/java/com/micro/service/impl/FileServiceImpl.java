package com.micro.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.chain.core.Bootstrap;
import com.micro.chain.core.HandlerInitializer;
import com.micro.chain.core.Pipeline;
import com.micro.chain.handler.AppMergeDelRedisHandler;
import com.micro.chain.handler.ChunkFileSuffixHandler;
import com.micro.chain.handler.ChunkRedisHandler;
import com.micro.chain.handler.ChunkStoreHandler;
import com.micro.chain.handler.ChunkValidateHandler;
import com.micro.chain.handler.CopyCapacityEnoughHandler;
import com.micro.chain.handler.CopyCapacityUpdateHandler;
import com.micro.chain.handler.CopyGetDataHandler;
import com.micro.chain.handler.CopySaveHandler;
import com.micro.chain.handler.CopySolrHandler;
import com.micro.chain.handler.CopyValidateHandler;
import com.micro.chain.handler.CreateCapacityEnoughHandler;
import com.micro.chain.handler.CreateCapacityUpdateHandler;
import com.micro.chain.handler.CreateExistHandler;
import com.micro.chain.handler.CreateFileCutHandler;
import com.micro.chain.handler.CreateSaveDiskFileHandler;
import com.micro.chain.handler.CreateSaveDiskMd5ChunkHandler;
import com.micro.chain.handler.CreateSaveDiskMd5Handler;
import com.micro.chain.handler.CreateSolrHandler;
import com.micro.chain.handler.CreateValidateHandler;
import com.micro.chain.handler.DiskCapacityUpdateHandler;
import com.micro.chain.handler.DiskSaveDiskFileEditHandler;
import com.micro.chain.handler.EditCapacityEnoughHandler;
import com.micro.chain.handler.EditExistHandler;
import com.micro.chain.handler.EditFileCutHandler;
import com.micro.chain.handler.EditSaveDiskFileHandler;
import com.micro.chain.handler.EditSaveDiskMd5ChunkHandler;
import com.micro.chain.handler.EditSaveDiskMd5Handler;
import com.micro.chain.handler.EditSolrHandler;
import com.micro.chain.handler.EditValidateHandler;
import com.micro.chain.handler.FileDelCapacityHandler;
import com.micro.chain.handler.FileDelDelHandler;
import com.micro.chain.handler.FileDelGetDataHandler;
import com.micro.chain.handler.FileDelRedisHandler;
import com.micro.chain.handler.FileDelSolrHandler;
import com.micro.chain.handler.FileDelValidateHandler;
import com.micro.chain.handler.FolderSaveHandler;
import com.micro.chain.handler.FolderValidateHandler;
import com.micro.chain.handler.FolerSolrHandler;
import com.micro.chain.handler.MergeCapacityIsEnoughHandler;
import com.micro.chain.handler.MergeCapacityUpdateHandler;
import com.micro.chain.handler.MergeCreateFolderHandler;
import com.micro.chain.handler.MergeDelRedisHandler;
import com.micro.chain.handler.MergeFileIsBreakHandler;
import com.micro.chain.handler.MergeFileIsExistHandler;
import com.micro.chain.handler.MergeGetChunkHandler;
import com.micro.chain.handler.MergeGlThumnailHandler;
import com.micro.chain.handler.MergeSaveDiskFileHandler;
import com.micro.chain.handler.MergeSaveDiskMd5ChunkHandler;
import com.micro.chain.handler.MergeSaveDiskMd5Handler;
import com.micro.chain.handler.MergeSolrHandler;
import com.micro.chain.handler.MergeSpecialDealHandler;
import com.micro.chain.handler.MergeValidateHander;
import com.micro.chain.handler.MoveSolrHandler;
import com.micro.chain.handler.MoveUpdateHandler;
import com.micro.chain.handler.MoveValidateHandler;
import com.micro.chain.handler.RenameIsExistHandler;
import com.micro.chain.handler.RenameSolrHandler;
import com.micro.chain.handler.RenameUpdateHandler;
import com.micro.chain.handler.RenameValidateHandler;
import com.micro.chain.handler.ShareSaveCapacityEnoughHandler;
import com.micro.chain.handler.ShareSaveCapacityUpdateHandler;
import com.micro.chain.handler.ShareSaveEffectHandler;
import com.micro.chain.handler.ShareSaveGetDataHandler;
import com.micro.chain.handler.ShareSaveInsertHandler;
import com.micro.chain.handler.ShareSaveLogHandler;
import com.micro.chain.handler.ShareSaveSolrHandler;
import com.micro.chain.handler.ShareSaveValidateHandler;
import com.micro.chain.param.ChunkRequest;
import com.micro.chain.param.CopyRequest;
import com.micro.chain.param.CreateRequest;
import com.micro.chain.param.CreateResponse;
import com.micro.chain.param.EditRequest;
import com.micro.chain.param.EditResponse;
import com.micro.chain.param.FileDelRequest;
import com.micro.chain.param.FolderRequest;
import com.micro.chain.param.MergeRequest;
import com.micro.chain.param.MoveRequest;
import com.micro.chain.param.RenameRequest;
import com.micro.chain.param.ShareSaveRequest;
import com.micro.common.CapacityUtils;
import com.micro.common.Contanst;
import com.micro.common.DateUtils;
import com.micro.common.IconConstant;
import com.micro.common.ValidateUtils;
import com.micro.db.dao.DiskAppFileDao;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.db.jdbc.DiskFileJdbc;
import com.micro.disk.bean.Chunk;
import com.micro.disk.bean.FileBean;
import com.micro.disk.bean.FileListBean;
import com.micro.disk.bean.FolderProp;
import com.micro.disk.bean.FolderTree;
import com.micro.disk.bean.MergeFileBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.FileService;
import com.micro.lock.LockContext;
import com.micro.model.DiskAppFile;
import com.micro.model.DiskFile;
import com.micro.model.DiskMd5;
import com.micro.utils.SpringContentUtils;

@Service(interfaceClass=FileService.class,timeout=120000)//毫秒1s=1000毫秒，1分钟=60s=60*1000毫秒
@Component
@Transactional
public class FileServiceImpl implements FileService{
	@Autowired
	private DiskFileDao diskFileDao;
	@Autowired
	private DiskFileJdbc diskFileJdbc;
	@Autowired
	private DiskMd5Dao diskMd5Dao;
	@Autowired
	private DiskAppFileDao diskAppFileDao;
	@Autowired
	private SpringContentUtils springContentUtils;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@NacosValue(value="${locktype}",autoRefreshed=true)
    private String locktype;
	
	@NacosValue(value="${lockhost}",autoRefreshed=true)
	private String lockhost;
	
	@Override
	public PageInfo<FileListBean> findPageList(Integer page, Integer limit, String userid, String pid, String typecode,String orderfield,String ordertype) {
		return diskFileJdbc.findAllList(page, limit, userid, pid, typecode,orderfield,ordertype);
	}
	
	@Override
	public PageInfo<FileListBean> findPageListCard(Integer page, Integer limit, String userid, String pid, String typecode,String orderfield,String ordertype) {
		return diskFileJdbc.findAllListCard(page, limit, userid, pid,orderfield,ordertype);
	}
	
	@Override
	public PageInfo<FileListBean> findSpecialList(Integer page, Integer limit, String userid, String typecode,String filesuffix,String filename,String showtype,String orderfield,String ordertype) {
		return diskFileJdbc.findSpecialList(page, limit, userid, typecode,filesuffix,filename, showtype,orderfield,ordertype);
	}

	@Override
	public FileBean findOne(String id) {
		DiskFile file=diskFileDao.findOne(id);
		FileBean bean=new FileBean();
		BeanUtils.copyProperties(file, bean);
		bean.setCreatetime(DateUtils.formatDate(file.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
		bean.setFilesize(CapacityUtils.convert(file.getFilesize()));
		return bean;
	}
	@Override
	public List<FileBean> findChildrenFiles(String userid,String pid){
		if(StringUtils.isEmpty(pid)){
			pid="0";
		}
		List<DiskFile> files=diskFileDao.findListByPid(userid,pid);
		List<FileBean> beans=new ArrayList<FileBean>();
		if(!CollectionUtils.isEmpty(files)){
			files.forEach(file->{
				FileBean bean=new FileBean();
				BeanUtils.copyProperties(file, bean);
				bean.setCreatetime(DateUtils.formatDate(file.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				bean.setFilesize(CapacityUtils.convert(file.getFilesize()));
				beans.add(bean);
			});
		}
		return beans;
	}
	
	@Override
	public void uploadChunk(Chunk chunk) {
		ChunkRequest request=new ChunkRequest();
		BeanUtils.copyProperties(chunk, request);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(ChunkValidateHandler.class));
				//2.校验是否支持该文件格式
				pipeline.addLast(springContentUtils.getHandler(ChunkFileSuffixHandler.class));
				//3.切块存储
				pipeline.addLast(springContentUtils.getHandler(ChunkStoreHandler.class));
				//4.把记录保存到Redis
				pipeline.addLast(springContentUtils.getHandler(ChunkRedisHandler.class));				
			}
		});
		bootstrap.execute();
	}
	
	@Override
	public Integer checkFile(String filemd5) {
		String lockname=filemd5;
		try{
			//lockContext.getLock(lockname);
			DiskMd5 diskMd5=diskMd5Dao.findMd5IsExist(filemd5);
			return diskMd5==null?0:1;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally{
			//lockContext.unLock(lockname);
		}
	}
	
	@Deprecated
	@Override
	public Integer checkChunk(String userid,String filemd5, Integer chunkNumber) {
		try{
			return null;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 加锁说明
	 * 1）如果是文件上传，lockname=${filemd5}
	 * 2）如果是文件夹上传，lockname=${userid}-${folderid}-${rootname}
	 * 这种模式，会存在以下两种bug
	 * bug1：
	 * 		* 释放锁时，由于事务没有提交，下一个获取锁的人，可能无法查询到上一个释放锁的人的数据，导致重复操作，导致失败
	 * 		* 【手工提交完成事务，才释放锁】
	 * 
	 * bug2：
	 * 		* 张三上传文件夹，文件夹里面有个文件1.png；李四上传文件1.png，可能会导致重复上传的情况
	 * 		* 这种是小概率事件，再说disk_md5表的md5字段是唯一性，其中一个报错，但是不会影响数据的一致性，只影响用户体验
	 */
	@Override
	public void mergeChunk(MergeFileBean bean) {
		String lockname=bean.getFilemd5();
		LockContext lockContext=new LockContext(locktype,lockhost);
		try{
			if(!StringUtils.isEmpty(bean.getRelativepath())){
				String[] names=bean.getRelativepath().split("/");
				String userid=bean.getUserid();
				String folderid=bean.getPid();
				
				lockname="CREATEFOLDER-"+userid+"-"+folderid+"-"+names[0];
			}
			
			//获取锁锁
			lockContext.getLock(lockname);
			
			MergeRequest request=new MergeRequest();
			BeanUtils.copyProperties(bean, request);
			
			Bootstrap bootstrap=new Bootstrap();
			bootstrap.childHandler(new HandlerInitializer(request,null) {
				@Override
				protected void initChannel(Pipeline pipeline) {
					//1.基本参数的校验
					pipeline.addLast(springContentUtils.getHandler(MergeValidateHander.class));
					//2.检验容量是否足够
					pipeline.addLast(springContentUtils.getHandler(MergeCapacityIsEnoughHandler.class));
					//3.从Redis获取切块记录
					pipeline.addLast(springContentUtils.getHandler(MergeGetChunkHandler.class));
					//4.校验文件是否完整
					pipeline.addLast(springContentUtils.getHandler(MergeFileIsBreakHandler.class));
					//5.查询文件是否已经在md5存在了
					pipeline.addLast(springContentUtils.getHandler(MergeFileIsExistHandler.class));					
					//6.保存disk_md5表
					pipeline.addLast(springContentUtils.getHandler(MergeSaveDiskMd5Handler.class));										
					//7.保存disk_chunk表
					pipeline.addLast(springContentUtils.getHandler(MergeSaveDiskMd5ChunkHandler.class));
					//8.如果是文件夹上传，则先创建文件夹
					pipeline.addLast(springContentUtils.getHandler(MergeCreateFolderHandler.class));
					//9.保存disk_file表
					pipeline.addLast(springContentUtils.getHandler(MergeSaveDiskFileHandler.class));
					//10.如果是图片则裁剪；如果是视频则截图
					pipeline.addLast(springContentUtils.getHandler(MergeSpecialDealHandler.class));					
					//11.如果是相册上传图片，则关联相册
					pipeline.addLast(springContentUtils.getHandler(MergeGlThumnailHandler.class));
					//12.更新容量、推送容量
					pipeline.addLast(springContentUtils.getHandler(MergeCapacityUpdateHandler.class));
					//13.新增Solr
					pipeline.addLast(springContentUtils.getHandler(MergeSolrHandler.class));
					//14.删除Redis记录
					//pipeline.addLast(springContentUtils.getHandler(MergeDelRedisHandler.class));		
				}
			});
			bootstrap.execute();
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}finally{
			try{
				String key=Contanst.PREFIX_CHUNK_TEMP+"-"+bean.getUserid()+"-"+bean.getUuid()+"-"+bean.getFileid()+"-"+bean.getFilename()+"-*";
				Set<String> keys = stringRedisTemplate.keys(key);
				stringRedisTemplate.delete(keys);
			}catch(Exception e){
				
			}finally{				
				lockContext.unLock(lockname);
			}
		}
	}
	
	
	@Override
	public void addFolder(String pid, String filename, String userid, String username) {
		FolderRequest request=new FolderRequest();
		request.setPid(pid);
		request.setFilename(filename);
		request.setUserid(userid);
		request.setUsername(username);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.基本校验
				pipeline.addLast(springContentUtils.getHandler(FolderValidateHandler.class));
				//2.保存disk_file
				pipeline.addLast(springContentUtils.getHandler(FolderSaveHandler.class));
				//3.新增Solr
				pipeline.addLast(springContentUtils.getHandler(FolerSolrHandler.class));
				//4.最近操作
			}
		});
		bootstrap.execute();
	}

	@Override
	public void rename(String userid,String id, String filename) {
		RenameRequest request=new RenameRequest();
		request.setFilename(filename);
		request.setId(id);
		request.setUserid(userid);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(RenameValidateHandler.class));
				//2.校验是否已经存在
				pipeline.addLast(springContentUtils.getHandler(RenameIsExistHandler.class));
				//3.修改disk_file
				pipeline.addLast(springContentUtils.getHandler(RenameUpdateHandler.class));
				//4.新增Solr
				pipeline.addLast(springContentUtils.getHandler(RenameSolrHandler.class));
				//5.最近操作
			}
		});
		bootstrap.execute();
	}
	
	@Override
	public void delete(String createuserid,String createusername,List<String> ids) {
		FileDelRequest request=new FileDelRequest();
		request.setIds(ids);
		request.setCreateuserid(createuserid);
		request.setCreateusername(createusername);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(FileDelValidateHandler.class));
				//2.递归获取被删除的所有数据
				pipeline.addLast(springContentUtils.getHandler(FileDelGetDataHandler.class));
				//3.递归删除并且往disk_file_del表新增
				pipeline.addLast(springContentUtils.getHandler(FileDelDelHandler.class));
				//4.删除Solr
				pipeline.addLast(springContentUtils.getHandler(FileDelSolrHandler.class));
				//5.把key添加到Redis进行监听
				pipeline.addLast(springContentUtils.getHandler(FileDelRedisHandler.class));
				//6.更新容量、推送容量
				pipeline.addLast(springContentUtils.getHandler(FileDelCapacityHandler.class));
				//7.最近操作
			}
		});
		bootstrap.execute();
	}
	
	@Deprecated
	@Override
	public List<FolderTree> findFolderTree(String userid,String pid,List<String> ids) {
		ValidateUtils.validate(userid, "用户ID");
		if(StringUtils.isEmpty(pid)){
			pid="0";
		}
		List<DiskFile> files=null;
		if(CollectionUtils.isEmpty(ids)){
			files=diskFileDao.findFolderTree(userid, pid);
		}else{
			files=diskFileJdbc.findFolderTree(userid, pid, ids);
		}
		
		List<FolderTree> trees=new ArrayList<FolderTree>();
		if(!CollectionUtils.isEmpty(files)){
			for(DiskFile file:files){
				FolderTree tree=new FolderTree();
				tree.setId(file.getId());
				tree.setLabel(file.getFilename());
				tree.setLeaf(false);
				tree.setChildren(new ArrayList<FolderTree>());
				trees.add(tree);
			}
		}
		return trees;
	}
	
	@Override
	public List<FileBean> findFolderList(String userid,String pid,List<String> ids){
		List<DiskFile> files=diskFileJdbc.findFolderTree(userid, pid, ids);
		List<FileBean> beans=new ArrayList<FileBean>();
		if(!CollectionUtils.isEmpty(files)){
			for(DiskFile file:files){
				FileBean bean=new FileBean();
				bean.setId(file.getId());
				bean.setPid(file.getPid());
				bean.setFileicon(IconConstant.icon);
				bean.setFilename(file.getFilename());
				bean.setFilesize("-");
				bean.setCreatetime(DateUtils.formatDate(file.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				beans.add(bean);
			}
		}
		return beans;
	}
	
	@Override
	public void copyTo(String userid,String username,List<String> ids,String folderid) {
		CopyRequest request=new CopyRequest();
		request.setFolderid(folderid);
		request.setIds(ids);
		request.setUserid(userid);
		request.setUsername(username);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(CopyValidateHandler.class));
				//2.获取有所数据
				pipeline.addLast(springContentUtils.getHandler(CopyGetDataHandler.class));
				//3.判断容量是否足够
				pipeline.addLast(springContentUtils.getHandler(CopyCapacityEnoughHandler.class));
				//4.开始复制
				pipeline.addLast(springContentUtils.getHandler(CopySaveHandler.class));
				//5.更新容量、推送容量
				pipeline.addLast(springContentUtils.getHandler(CopyCapacityUpdateHandler.class));
				//6.新增Solr
				pipeline.addLast(springContentUtils.getHandler(CopySolrHandler.class));
				//7.最近操作
			}
		});
		bootstrap.execute();
	}
	
	@Override
	public void moveTo(String userid,List<String> ids,String folderid) {
		MoveRequest request=new MoveRequest();
		request.setFolderid(folderid);
		request.setIds(ids);
		request.setUserid(userid);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(MoveValidateHandler.class));
				//2.移动操作
				pipeline.addLast(springContentUtils.getHandler(MoveUpdateHandler.class));
				//3.更新Solr
				pipeline.addLast(springContentUtils.getHandler(MoveSolrHandler.class));
				//4.最近操作
			}
		});
		bootstrap.execute();
	}
	
	@Override
	public void saveFromShare(String userid,String username, String folderid,String shareid, List<String> ids) {
		ShareSaveRequest request=new ShareSaveRequest();
		shareid=shareid.toLowerCase();
		request.setUserid(userid);
		request.setUsername(username);
		request.setFolderid(folderid);
		request.setShareid(shareid);
		request.setIds(ids);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//1.参数校验
				pipeline.addLast(springContentUtils.getHandler(ShareSaveValidateHandler.class));
				//2.校验分享是否有效
				pipeline.addLast(springContentUtils.getHandler(ShareSaveEffectHandler.class));
				//3.递归获取数据
				pipeline.addLast(springContentUtils.getHandler(ShareSaveGetDataHandler.class));
				//4.判断容量是否足够
				pipeline.addLast(springContentUtils.getHandler(ShareSaveCapacityEnoughHandler.class));
				//5.保存
				pipeline.addLast(springContentUtils.getHandler(ShareSaveInsertHandler.class));
				//6.保存Solr
				pipeline.addLast(springContentUtils.getHandler(ShareSaveSolrHandler.class));
				//7.更新容量、推送
				pipeline.addLast(springContentUtils.getHandler(ShareSaveCapacityUpdateHandler.class));
				//8.分享日志（转存数量、转存明细）
				pipeline.addLast(springContentUtils.getHandler(ShareSaveLogHandler.class));
				//9.最近操作
			}
		});
		bootstrap.execute();
	}
	
	@Override
	public List<Map<String, Object>> findParentListByPid(String pid) {
		List<Map<String, Object>> lists=new ArrayList<>();
		if("0".equals(pid)){
			return lists;
		}		
		DiskFile file=diskFileDao.findOne(pid);
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", pid);
		map.put("name", file.getFilename());
		lists.add(map);
		dgFindParentList(file.getPid(), lists);
		
		return lists;
	}
	
	@Override
	public List<Map<String, Object>> findParentListById(String id) {
		List<Map<String, Object>> lists=new ArrayList<>();
		DiskFile file=diskFileDao.findOne(id);
		if(!"0".equals(file.getPid())){
			dgFindParentList(file.getPid(), lists);
		}
		return lists;
	}

	@Override
	public FolderProp findFolderProp(String userid,String id) {
		FolderProp fp=new FolderProp();
		fp.setFilenum(0);
		fp.setFoldernum(0);
		fp.setTotalsize(0l);
		
		dgFindFolderProp(userid,id, fp);
		
		fp.setTotalsizename(CapacityUtils.convert(fp.getTotalsize()));
		return fp;
	}
	private void dgFindParentList(String pid,List<Map<String, Object>> lists){
		if(!"0".equals(pid)){			
			DiskFile file=diskFileDao.findOne(pid);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", pid);
			map.put("name", file.getFilename());
			lists.add(0, map);
			
			dgFindParentList(file.getPid(), lists);
		}
	}
	private void dgFindFolderProp(String userid,String pid,FolderProp fp){
		List<DiskFile> files=diskFileDao.findListByPid(userid,pid);
		if(!CollectionUtils.isEmpty(files)){
			for(DiskFile file:files){
				fp.setTotalsize(fp.getTotalsize()+file.getFilesize());
				if(file.getFiletype()==1){
					fp.setFilenum(fp.getFilenum()+1);
				}else if(file.getFiletype()==0){
					fp.setFoldernum(fp.getFoldernum()+1);
				}
				dgFindFolderProp(userid,file.getId(), fp);
			}
		}
	}

	@Override
	public FileBean editFile(String fileid, byte[] bytes) {
		String filemd5=DigestUtils.md5DigestAsHex(bytes);
		LockContext lockContext=new LockContext(locktype,lockhost);
		try{
			//加分布式锁，让md5相同的文件排队执行
			lockContext.getLock(filemd5);
			
			EditRequest request=new EditRequest();
			request.setBytes(bytes);
			request.setFileid(fileid);
			request.setFilemd5(filemd5);
			
			Bootstrap bootstrap=new Bootstrap();
			bootstrap.childHandler(new HandlerInitializer(request,new EditResponse()) {
				@Override
				protected void initChannel(Pipeline pipeline) {
					//1.参数校验
					pipeline.addLast(springContentUtils.getHandler(EditValidateHandler.class));
					//2.判断容量是否足够
					pipeline.addLast(springContentUtils.getHandler(EditCapacityEnoughHandler.class));
					//3.判断MD5是否存在
					pipeline.addLast(springContentUtils.getHandler(EditExistHandler.class));
					//4.文件切分并且上传FastDFS				
					pipeline.addLast(springContentUtils.getHandler(EditFileCutHandler.class));
					//5.保存disk_md5
					pipeline.addLast(springContentUtils.getHandler(EditSaveDiskMd5Handler.class));
					//6.保存disk_md5_chunk
					pipeline.addLast(springContentUtils.getHandler(EditSaveDiskMd5ChunkHandler.class));
					//7.保存disk_file
					pipeline.addLast(springContentUtils.getHandler(EditSaveDiskFileHandler.class));
					//8.保存disk_file_edit
					pipeline.addLast(springContentUtils.getHandler(DiskSaveDiskFileEditHandler.class));
					//9.新增Solr
					pipeline.addLast(springContentUtils.getHandler(EditSolrHandler.class));
					//10.更新容量、容量推送
					pipeline.addLast(springContentUtils.getHandler(DiskCapacityUpdateHandler.class));
					//11.最近操作
					
				}
			});
			EditResponse res=(EditResponse) bootstrap.execute();
			
			FileBean fb=new FileBean();
			fb.setId(fileid);
			fb.setFilename(res.getFilename());
			fb.setFilemd5(filemd5);
			
			return fb;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}finally{
			//释放分布式锁
			lockContext.unLock(filemd5);
		}
	}

	@Override
	public FileBean addFile(String pid,String filename, byte[] bytes, String userid, String username) {
		String filemd5=DigestUtils.md5DigestAsHex(bytes);
		LockContext lockContext=new LockContext(locktype,lockhost);
		try{
			//加分布式锁，让md5相同的文件排队执行
			lockContext.getLock(filemd5);
			
			CreateRequest request=new CreateRequest();
			request.setBytes(bytes);
			request.setFilename(filename);
			request.setPid(pid);
			request.setUserid(userid);
			request.setUsername(username);
			request.setFilemd5(filemd5);
			
			Bootstrap bootstrap=new Bootstrap();
			bootstrap.childHandler(new HandlerInitializer(request,new CreateResponse()) {
				@Override
				protected void initChannel(Pipeline pipeline) {
					//1.参数校验
					pipeline.addLast(springContentUtils.getHandler(CreateValidateHandler.class));
					//2.判断容量是否足够
					pipeline.addLast(springContentUtils.getHandler(CreateCapacityEnoughHandler.class));
					//3.判断MD5是否存在
					pipeline.addLast(springContentUtils.getHandler(CreateExistHandler.class));
					//4.文件切分并且上传FastDFS	
					pipeline.addLast(springContentUtils.getHandler(CreateFileCutHandler.class));
					//5.保存disk_md5
					pipeline.addLast(springContentUtils.getHandler(CreateSaveDiskMd5Handler.class));
					//6.保存disk_md5_chunk
					pipeline.addLast(springContentUtils.getHandler(CreateSaveDiskMd5ChunkHandler.class));
					//7.保存disk_file
					pipeline.addLast(springContentUtils.getHandler(CreateSaveDiskFileHandler.class));
					//8.新增Solr
					pipeline.addLast(springContentUtils.getHandler(CreateSolrHandler.class));
					//9.更新容量、容量推送
					pipeline.addLast(springContentUtils.getHandler(CreateCapacityUpdateHandler.class));
					//10.最近操作
				}
			});
			CreateResponse res=(CreateResponse) bootstrap.execute();
			
			FileBean fb=new FileBean();
			fb.setId(res.getFileid());
			fb.setFilename(filename);
			fb.setFilemd5(filemd5);
			return fb;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}finally{
			//释放分布式锁
			lockContext.unLock(filemd5);
		}	
	}

	@Override
	public void addFromAppFile(String fileId, String targetFolderId, String userId, String userName) {
		//1.查询
		DiskAppFile daf=diskAppFileDao.findOne(fileId);
		if(daf==null){
			throw new RuntimeException("fileId="+fileId+",在应用文件里面不存在");
		}
		//2.判断targetFolderId是否是文件夹
		if(StringUtils.isEmpty(targetFolderId)){
			targetFolderId="0";
		}
		if(!"0".equals(targetFolderId)){
			DiskFile df = diskFileDao.findOne(targetFolderId);			
			if(df.getFiletype()==1){
				throw new RuntimeException("targetFolderId="+targetFolderId+",对应的是文件不是文件夹!");
			}
		}
		//3.查询md5
		DiskMd5 dm = diskMd5Dao.findMd5IsExist(daf.getFilemd5());
		//4.保存
		DiskFile file=diskFileDao.findFile(userId, targetFolderId, daf.getFilename(),daf.getFilemd5());
		if(file==null){
			file=new DiskFile();
			file.setPid(targetFolderId);
			file.setFilename(daf.getFilename());
			file.setFilesize(daf.getFilesize());
			file.setFilesuffix(daf.getFilesuffix());
			file.setTypecode(daf.getTypecode());
			file.setFilemd5(daf.getFilemd5());
			file.setFiletype(1);
			file.setCreateuserid(userId);
			file.setCreateusername(userName);
			file.setCreatetime(new Date());
			file.setThumbnailurl(dm.getThumbnailurl());
			file.setImgsize(dm.getImgsize());
			
			diskFileDao.save(file);
		}
	}

	
	
}

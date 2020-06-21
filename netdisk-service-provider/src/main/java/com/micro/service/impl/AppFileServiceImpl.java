package com.micro.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.chain.core.Bootstrap;
import com.micro.chain.core.HandlerInitializer;
import com.micro.chain.core.Pipeline;
import com.micro.chain.handler.AppChunkRecordHandler;
import com.micro.chain.handler.AppChunkStoreHandler;
import com.micro.chain.handler.AppChunkValidateHandler;
import com.micro.chain.handler.AppMergeDelRedisHandler;
import com.micro.chain.handler.AppMergeGetRecordHandler;
import com.micro.chain.handler.AppMergeIsFullHandler;
import com.micro.chain.handler.AppMergeMd5IsExistHandler;
import com.micro.chain.handler.AppMergeSaveFileHandler;
import com.micro.chain.handler.AppMergeSaveMd5ChunkHandler;
import com.micro.chain.handler.AppMergeSaveMd5Handler;
import com.micro.chain.handler.AppMergeSaveSolrHandler;
import com.micro.chain.handler.AppMergeSpecialDealHandler;
import com.micro.chain.handler.AppMergeValidateHandler;
import com.micro.chain.param.AppChunkRequest;
import com.micro.chain.param.AppMergeRequest;
import com.micro.chain.param.AppMergeResponse;
import com.micro.common.Contanst;
import com.micro.common.ValidateUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.db.dao.DiskAppFileDao;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.db.dao.DiskTypeSuffixDao;
import com.micro.db.jdbc.DiskAppFileJdbc;
import com.micro.disk.bean.AppFileBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.AppFileService;
import com.micro.lock.LockContext;
import com.micro.model.DiskAppFile;
import com.micro.model.DiskFile;
import com.micro.model.DiskMd5;
import com.micro.model.DiskTypeSuffix;
import com.micro.utils.SpringContentUtils;


@Service(interfaceClass=AppFileService.class)
@Component
@Transactional
public class AppFileServiceImpl implements AppFileService{
	@Autowired
	private DiskAppFileDao diskAppFileDao;
	@Autowired
	private DiskAppFileJdbc diskAppFileJdbc;
	@Autowired
	private DiskMd5Dao diskMd5Dao;
	@Autowired
	private DiskFileDao diskFileDao;
	@Autowired
	private SpringContentUtils scu;
	private JsonUtils jsonUtils=new JsonJackUtils();
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private DiskTypeSuffixDao diskTypeSuffixDao;
	
	@NacosValue(value="${locktype}",autoRefreshed=true)
    private String locktype;
	
	@NacosValue(value="${lockhost}",autoRefreshed=true)
	private String lockhost;
	
	@Override
	public Integer checkFileByMd5(String filemd5) {
		DiskMd5 bean=diskMd5Dao.findMd5IsExist(filemd5);
		return bean==null?0:1;
	}

	@Override
	public void uploadChunk(byte[] bytes,String appId,String filemd5, String filename, int chunknum, String userid) {
		AppChunkRequest request=new AppChunkRequest();
		request.setBytes(bytes);
		request.setAppId(appId);
		request.setFilemd5(filemd5);
		request.setFilename(filename);
		request.setChunk(chunknum);
		request.setUserid(userid);
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.childHandler(new HandlerInitializer(request,null) {
			@Override
			protected void initChannel(Pipeline pipeline) {
				//参数校验
				pipeline.addLast(scu.getHandler(AppChunkValidateHandler.class));				
				//切块存储
				pipeline.addLast(scu.getHandler(AppChunkStoreHandler.class));				
				//切块记录存储
				pipeline.addLast(scu.getHandler(AppChunkRecordHandler.class));								
			}
		});
		bootstrap.execute();
		
	}

	@Override
	public String mergeChunk(String appId,String filemd5, String filename,long filesize, String businessid, String businesstype, String userid,
			String username,Boolean secondUpload, Boolean allowMultiple) {
		//分布式锁
		ValidateUtils.validate(filemd5, "文件MD5");
		LockContext lockContext=new LockContext(locktype,lockhost);
		String lockname=filemd5;
		try{
			//获取锁锁
			lockContext.getLock(lockname);
			
			//组装参数
			AppMergeRequest request=new AppMergeRequest();
			request.setAppId(appId);
			request.setFilemd5(filemd5);
			request.setFilename(filename);
			request.setFilesize(filesize);
			request.setBusinessid(businessid);
			request.setBusinesstype(businesstype);
			request.setUserid(userid);
			request.setUsername(username);
			request.setSecondUpload(secondUpload);
			request.setAllowMultiple(allowMultiple);
			
			Bootstrap bootstrap=new Bootstrap();
			bootstrap.childHandler(new HandlerInitializer(request,new AppMergeResponse()) {
				@Override
				protected void initChannel(Pipeline pipeline) {
					//参数校验
					pipeline.addLast(scu.getHandler(AppMergeValidateHandler.class));								
					//从Redis获取切块记录【如果是秒传则没有对应记录！！！！！】
					pipeline.addLast(scu.getHandler(AppMergeGetRecordHandler.class));								
					//校验文件是否完整
					pipeline.addLast(scu.getHandler(AppMergeIsFullHandler.class));								
					//查询md5是否存在
					pipeline.addLast(scu.getHandler(AppMergeMd5IsExistHandler.class));								
					//保存disk_md5
					pipeline.addLast(scu.getHandler(AppMergeSaveMd5Handler.class));								
					//保存disk_md5_chunk
					pipeline.addLast(scu.getHandler(AppMergeSaveMd5ChunkHandler.class));								
					//保存disk_app_file
					pipeline.addLast(scu.getHandler(AppMergeSaveFileHandler.class));		
					//如果是图片和视频则做特殊处理
					pipeline.addLast(scu.getHandler(AppMergeSpecialDealHandler.class));		
					//新增Solr【未实现，后面并发高了再实现吧】【在Solr新建对应的SolrCore，配置xml字段】
					pipeline.addLast(scu.getHandler(AppMergeSaveSolrHandler.class));
					//删除Redis记录
					//pipeline.addLast(scu.getHandler(AppMergeDelRedisHandler.class));					
				}
			});
			AppMergeResponse res=(AppMergeResponse) bootstrap.execute();
			return res.getFileid();
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}finally {
			try{
				String key=Contanst.PREFIX_CHUNK_TEMP+"-"+userid+"-"+filemd5+"-"+filename+"-*";
				Set<String> keys = stringRedisTemplate.keys(key);
				stringRedisTemplate.delete(keys);
			}catch(Exception e){
				e.printStackTrace();
			}finally{				
				//释放外层锁
				lockContext.unLock(lockname);
			}
			
		}
	}

	@Override
	public void fileHasBreak(String appId, String businessid, String businesstype, String filemd5) {
		ValidateUtils.validate(appId, "appId");
		ValidateUtils.validate(businessid, "businessid");
		ValidateUtils.validate(businesstype, "businesstype");
		ValidateUtils.validate(filemd5, "filemd5");
		
		diskAppFileDao.updateIsBreak(appId, businessid, businesstype, filemd5);
	}
	
	@Override
	public void editFile(byte[] bytes, String fileid) {
		
	}

	@Override
	public PageInfo<AppFileBean> findFiles(Integer page,Integer limit,String appid,String userid,String username,String filename,String filemd5,Boolean isAdmin){
		if(isAdmin==null){
			isAdmin=true;
		}
		return diskAppFileJdbc.findFiles(page, limit, appid, userid, username, filename, filemd5,isAdmin);
	}

	@Override
	public List<AppFileBean> findFiles(String appId,String businessid) {
		List<DiskAppFile> lists=diskAppFileDao.findListByBusinessid(appId,businessid);
		List<AppFileBean> beans=new ArrayList<AppFileBean>();
		if(!CollectionUtils.isEmpty(lists)){
			lists.forEach(bean->{
				AppFileBean fileBean=new AppFileBean();
				BeanUtils.copyProperties(bean, fileBean);
				beans.add(fileBean);
			});
		}
		return beans;
	}

	@Override
	public List<AppFileBean> findFiles(String appId,String businessid, String businesstype) {
		List<DiskAppFile> lists=diskAppFileDao.findListByBusinessidAndBusinesstype(appId,businessid,businesstype);
		List<AppFileBean> beans=new ArrayList<AppFileBean>();
		if(!CollectionUtils.isEmpty(lists)){
			lists.forEach(bean->{
				AppFileBean fileBean=new AppFileBean();
				BeanUtils.copyProperties(bean, fileBean);
				beans.add(fileBean);
			});
		}
		return beans;
	}

	@Override
	public void delete(String fileid) {
		diskAppFileDao.updateDelStatus(fileid);		
	}

	@Override
	public boolean hasSupportSuffix(String filesuffix) {
		//先去Redis查询，如果不存在，再去MySQL查询
		Object obj=stringRedisTemplate.opsForValue().get(Contanst.PREFIX_TYPE_SUFFIX+filesuffix);
		if(obj==null){				
			DiskTypeSuffix dts=diskTypeSuffixDao.findBySuffix(filesuffix);
			if(dts==null){
				return false;
			}else{
				stringRedisTemplate.opsForValue().set(Contanst.PREFIX_TYPE_SUFFIX+filesuffix, jsonUtils.objectToJson(dts));					
				return true;
			}
		}else{
			return true;
		}
	}

	@Override
	public void saveToApplNetdisk(String appId,String businessId,String businessType,String fileId, String userId, String userName) {
		ValidateUtils.validate(fileId,"fileId");
		ValidateUtils.validate(userId,"userId");
		ValidateUtils.validate(userName,"userName");
		
		DiskFile df = diskFileDao.findOne(fileId);
		if(df==null){
			throw new RuntimeException("fileId"+fileId+"不存在");
		}
		if(df.getFiletype()==0){
			throw new RuntimeException("fileId"+fileId+",对应的是文件夹,无法转存!");
		}
		
		Integer count=diskAppFileDao.findRecordIsExist(appId, businessId, businessType, df.getFilemd5(), df.getFilename());
		if(count==null||count==0){
			DiskAppFile file=new DiskAppFile();
			file.setAppid(appId);
			file.setBusinessid(businessId);
			file.setBusinesstype(businessType);
			file.setFilename(df.getFilename());
			file.setFilesize(df.getFilesize());
			file.setFilesuffix(df.getFilesuffix());
			file.setTypecode(df.getTypecode());
			file.setFilemd5(df.getFilemd5());
			file.setDelstatus(0);
			file.setCreateuserid(userId);
			file.setCreateusername(userName);
			file.setCreatetime(new Date());
			file.setIsbreak(0);
			diskAppFileDao.save(file);
		}
	}

}

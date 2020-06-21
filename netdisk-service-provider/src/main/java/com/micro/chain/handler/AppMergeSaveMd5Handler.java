package com.micro.chain.handler;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.AppMergeRequest;
import com.micro.chain.param.MergeRequest;
import com.micro.common.Contanst;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.db.dao.DiskTypeSuffixDao;
import com.micro.model.DiskMd5;
import com.micro.model.DiskTypeSuffix;

@Component
public class AppMergeSaveMd5Handler extends Handler{
	@Autowired
	private DiskMd5Dao diskMd5Dao;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private DiskTypeSuffixDao diskTypeSuffixDao;
	private JsonUtils jsonUtils=new JsonJackUtils();
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof AppMergeRequest){
			AppMergeRequest bean=(AppMergeRequest) request;
			if(!bean.isMd5isExist()){//在disk_md5中不存在	
				String filesuffix=FilenameUtils.getExtension(bean.getFilename());
				String typecode="";
				
				//先去Redis查询，如果不存在，再去MySQL查询
				DiskTypeSuffix dts=null;
				Object obj=stringRedisTemplate.opsForValue().get(Contanst.PREFIX_TYPE_SUFFIX+filesuffix);
				if(obj==null){				
					dts=diskTypeSuffixDao.findBySuffix(filesuffix);
					if(dts==null){
						throw new RuntimeException("网盘暂时不支持该格式,请联系管理员添加!"+filesuffix);
					}else{
						stringRedisTemplate.opsForValue().set(Contanst.PREFIX_TYPE_SUFFIX+filesuffix, jsonUtils.objectToJson(dts));					
					}
				}else{
					dts=jsonUtils.jsonToPojo(obj.toString(), DiskTypeSuffix.class);
				}
				typecode=dts.getTypecode();
				
				DiskMd5 diskMd5=new DiskMd5();
				diskMd5.setMd5(bean.getFilemd5());
				diskMd5.setFilename(bean.getFilename());
				diskMd5.setFilesize(bean.getFilesize());
				diskMd5.setFilenum(bean.getTemps().size());
				diskMd5.setTypecode(typecode);
				diskMd5.setFilesuffix(filesuffix);
				diskMd5Dao.save(diskMd5);
				
				//写到下一个Handler
				bean.setFilesuffix(filesuffix);
				bean.setTypecode(typecode);
				this.updateRequest(bean);
			}
			
		}else{
			throw new RuntimeException("AppMergeSaveMd5Handler==参数不对");
		}		
	}

}

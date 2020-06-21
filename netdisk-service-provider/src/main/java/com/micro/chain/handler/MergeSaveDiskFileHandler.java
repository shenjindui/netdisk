package com.micro.chain.handler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MergeRequest;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;

@Component
public class MergeSaveDiskFileHandler extends Handler {
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MergeRequest){
			try{
				MergeRequest bean=(MergeRequest) request;
				
				DiskFile file=diskFileDao.findFile(bean.getUserid(), bean.getPid(), bean.getFilename(), bean.getFilemd5());
				String diskfileid="";
				
				//md5->disk_md5不存在->disk_file不存在，裁剪
				//md5->disk_md5存在->disk_file不存在，不裁剪
				//md5->disk_md5存在->disk_file存在，不裁剪
				
				if(bean.isExistindiskmd5()){				
					if(file==null){
						DiskFile df=new DiskFile();
						df.setPid(bean.getPid());
						df.setFilename(bean.getFilename());
						df.setFilesize(bean.getTotalSize());
						df.setFilesuffix(bean.getFilesuffix());
						df.setTypecode(bean.getTypecode());
						df.setFilemd5(bean.getFilemd5());
						df.setFiletype(1);
						df.setCreateuserid(bean.getUserid());
						df.setCreateusername(bean.getUsername());
						df.setCreatetime(new Date());
						df.setThumbnailurl(bean.getThumbnailurl());//裁剪图片
						df.setImgsize(bean.getImgsize());//裁剪图片
						diskFileDao.save(df);
						
						diskfileid=df.getId();
					}else{
						diskfileid=file.getId();
					}
				}else{
					if(file==null){
						DiskFile df=new DiskFile();
						df.setPid(bean.getPid());
						df.setFilename(bean.getFilename());
						df.setFilesize(bean.getTotalSize());
						df.setFilesuffix(bean.getFilesuffix());
						df.setTypecode(bean.getTypecode());
						df.setFilemd5(bean.getFilemd5());
						df.setFiletype(1);
						df.setCreateuserid(bean.getUserid());
						df.setCreateusername(bean.getUsername());
						df.setCreatetime(new Date());
						//df.setThumbnailurl(bean.getThumbnailurl());//裁剪图片
						//df.setImgsize(bean.getImgsize());//裁剪图片
						diskFileDao.save(df);
						
						diskfileid=df.getId();
					}else{
						throw new RuntimeException("该filemd5不存在于disk_md5表，但是存在于disk_file表，不符合规则！");
					}
				}
				
				//写到下一个Handler
				bean.setDiskfileid(diskfileid);
				bean.setExistindiskfile(file==null?false:true);
				this.updateRequest(bean);
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException("MergeSaveDiskFileHandler=="+e.getMessage());
			}
		}else{
			throw new RuntimeException("MergeSaveDiskFileHandler==参数不对");
		}		
	}

}

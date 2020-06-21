package com.micro.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.micro.db.dao.DiskAppFileDao;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskMd5ChunkDao;
import com.micro.disk.bean.DownloadBean;
import com.micro.disk.service.FilePreviewService;
import com.micro.model.DiskAppFile;
import com.micro.model.DiskFile;
import com.micro.model.DiskMd5Chunk;
import com.micro.store.context.StoreContext;
import com.micro.store.service.StoreService;

@Service(interfaceClass=FilePreviewService.class,timeout=120000)//毫秒1s=1000毫秒，1分钟=60s=60*1000毫秒
@Component
@Transactional
public class FilePreviewServiceImpl implements FilePreviewService{
	@Autowired
	private StoreContext storeContext;
	@Autowired
	private DiskMd5ChunkDao diskMd5ChunkDao;
	@Autowired
	private DiskFileDao diskFileDao;
	@Autowired
	private DiskAppFileDao diskAppFileDao;
	
	@Override
	public byte[] getBytesByUrl(String url) {
		return storeContext.download(url);
	}
	
	@Override
	public List<String> getChunksByFileid(String fileid) {
		DiskFile df=diskFileDao.findOne(fileid);
		if(df==null){
			throw new RuntimeException("fileid="+fileid+"不存在");
		}
		return getChunksByFilemd5(df.getFilemd5());
	}
	
	@Override
	public List<String> getChunksByAppFileid(String fileid) {
		DiskAppFile daf=diskAppFileDao.findOne(fileid);
		if(daf==null){
			throw new RuntimeException("fileid="+fileid+"不存在");
		}
		return getChunksByFilemd5(daf.getFilemd5());
	}

	@Override
	public List<String> getChunksByFilemd5(String filemd5) {
		List<DiskMd5Chunk> chunks=diskMd5ChunkDao.findList(filemd5);
		List<String> urls=new ArrayList<>();
		if(!CollectionUtils.isEmpty(chunks)){
			for(DiskMd5Chunk chunk:chunks){
				urls.add(chunk.getStorepath());
			}
		}
		return urls;
	}

	@Override
	public DownloadBean getDownloadInfo(String userid,List<String> fileids) {
		if(CollectionUtils.isEmpty(fileids)){
			throw new RuntimeException("请选择下载记录");
		}
		DownloadBean dd=new DownloadBean();
		dd.setFilenum(0);
		dd.setFoldernum(0);
		dd.setTotalsize(0l);
		
		for(String fileid:fileids){
			DiskFile file=diskFileDao.findOne(fileid);
			dd.setTotalsize(dd.getTotalsize()+file.getFilesize());
			if(file.getFiletype()==1){ //文件
				dd.setFilenum(dd.getFilenum()+1);
			}else if(file.getFiletype()==0){ //文件夹
				dd.setFoldernum(dd.getFoldernum()+1);
			}
			dgGetDownloadInfo(userid,file.getId(), dd);
		}
		return dd;
	}
	public void dgGetDownloadInfo(String userid,String pid,DownloadBean dd){
		List<DiskFile> files=diskFileDao.findListByPid(userid,pid);
		if(!CollectionUtils.isEmpty(files)){
			for(DiskFile file:files){
				dd.setTotalsize(dd.getTotalsize()+file.getFilesize());
				if(file.getFiletype()==1){
					dd.setFilenum(dd.getFilenum()+1);
				}else if(file.getFiletype()==0){
					dd.setFoldernum(dd.getFoldernum()+1);
				}
				dgGetDownloadInfo(userid,file.getId(), dd);
			}
		}
	}
}

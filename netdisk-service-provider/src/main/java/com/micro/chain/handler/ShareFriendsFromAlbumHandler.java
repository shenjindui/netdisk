package com.micro.chain.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.ShareFriendsRequest;
import com.micro.common.DateUtils;
import com.micro.db.dao.DiskAlbumDao;
import com.micro.db.dao.DiskShareFileDao;
import com.micro.db.jdbc.DiskAlbumJdbc;
import com.micro.disk.bean.FileBean;
import com.micro.model.DiskAlbum;
import com.micro.model.DiskShareFile;

@Component
public class ShareFriendsFromAlbumHandler extends Handler{
	@Autowired
	private DiskAlbumDao diskAlbumDao;
	@Autowired
	private DiskShareFileDao diskShareFileDao;
	@Autowired
	private DiskAlbumJdbc diskAlbumJdbc;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareFriendsRequest){
			ShareFriendsRequest bean=(ShareFriendsRequest) request;
			if(bean.getType()==1){
				List<String> ids=bean.getIds();
				
				for(String id:ids){
					DiskAlbum album=diskAlbumDao.findOne(id);
					if(album==null){
						throw new RuntimeException("分享的记录不存在,id="+id);
					}
					DiskShareFile shareFile=new DiskShareFile();
					shareFile.setShareid(bean.getShareid());
					shareFile.setPid("0");
					shareFile.setFilename(album.getAlbumname());
					shareFile.setFiletype(0);//0文件夹
					shareFile.setCreatetime(album.getCreatetime());
					diskShareFileDao.save(shareFile);
					
					dgAlbum(id,shareFile.getId() ,bean.getShareid());
				}
			}
		}else{
			throw new RuntimeException("ShareFriendsFromAlbumHandler==参数不对");
		}
	}
	private void dgAlbum(String albumid,String sharepid,String shareid){
		List<FileBean> files=diskAlbumJdbc.findInAlbumImg(albumid);
		if(!CollectionUtils.isEmpty(files)){
			for(FileBean file:files){
				DiskShareFile shareFile=new DiskShareFile();
				shareFile.setShareid(shareid);
				shareFile.setPid(sharepid);
				shareFile.setFilename(file.getFilename());
				shareFile.setFilesize(Long.parseLong(file.getFilesize()));
				shareFile.setFilesuffix(file.getFilesuffix());
				shareFile.setTypecode(file.getTypecode());
				shareFile.setFilemd5(file.getFilemd5());
				shareFile.setFiletype(1);//1文件
				shareFile.setCreateuserid(file.getCreateuserid());
				shareFile.setCreateusername(file.getCreateusername());
				shareFile.setCreatetime(DateUtils.parseDate(file.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				shareFile.setThumbnailurl(file.getThumbnailurl());
				shareFile.setImgsize(file.getImgsize());
				diskShareFileDao.save(shareFile);
			}
		}
	}
}

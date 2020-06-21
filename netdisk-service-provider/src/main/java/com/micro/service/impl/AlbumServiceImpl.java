package com.micro.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.micro.common.ValidateUtils;
import com.micro.db.dao.DiskAlbumDao;
import com.micro.db.dao.DiskAlbumFileDao;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.db.jdbc.DiskAlbumJdbc;
import com.micro.disk.bean.AlbumBean;
import com.micro.disk.bean.FileBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.AlbumService;
import com.micro.disk.service.FileService;
import com.micro.model.DiskAlbum;
import com.micro.model.DiskAlbumFile;
import com.micro.model.DiskFile;
import com.micro.model.DiskMd5;

@Service(interfaceClass=AlbumService.class)
@Component
@Transactional
public class AlbumServiceImpl implements AlbumService{
	@Autowired
	private DiskAlbumDao diskAlbumDao;
	@Autowired
	private DiskFileDao diskFileDao;
	@Autowired
	private DiskMd5Dao diskMd5Dao;
	@Autowired
	private DiskAlbumJdbc diskAlbumJdbc;
	@Autowired
	private DiskAlbumFileDao diskAlbumFileDao;
	@Autowired
	private FileService fileService;
	
	@Override
	public PageInfo<AlbumBean> findPageList(Integer page, Integer limit, String userid, String albumname) {
		return diskAlbumJdbc.findList(page, limit, userid, albumname);
	}

	@Override
	public List<AlbumBean> findList(String userid) {
		
		return diskAlbumJdbc.findList(userid);
	}

	@Override
	public AlbumBean findOne(String albumid) {
		DiskAlbum da=diskAlbumDao.findOne(albumid);
		if(da==null){
			throw new RuntimeException("相册ID不存在");
		}
		AlbumBean bean=new AlbumBean();
		bean.setId(da.getId());
		bean.setAlbumname(da.getAlbumname());
		bean.setAlbumdesc(da.getAlbumdesc());
		bean.setCoverurl(da.getCoverurl());
		return bean;
	}

	@Override
	public void addAlbum(String albumname, String albumdesc, String userid, String username) {
		ValidateUtils.validate(albumname, "相册名称");
		ValidateUtils.validate(userid, "创建人ID");
		ValidateUtils.validate(username, "创建人姓名");
		
		DiskAlbum album=new DiskAlbum();
		album.setAlbumname(albumname);
		album.setAlbumdesc(albumdesc);
		album.setCreateuserid(userid);
		album.setCreateusername(username);
		album.setCreatetime(new Date());
		diskAlbumDao.save(album);
	}

	@Override
	public void updateAlbum(String albumid, String albumname, String albumdesc) {
		ValidateUtils.validate(albumid, "相册ID");
		ValidateUtils.validate(albumname, "相册名称");
		DiskAlbum da=diskAlbumDao.findOne(albumid);
		if(da==null){
			throw new RuntimeException("相册ID不存在");
		}
		da.setAlbumname(albumname);
		da.setAlbumdesc(albumdesc);
		diskAlbumDao.save(da);
	}

	@Override
	public void deleteAlbum(String albumid) {
		diskAlbumDao.delete(albumid);
		diskAlbumFileDao.deleteByAlbumid(albumid);
	}

	@Override
	public void deleteAlbumCascade(String userid,String username,String albumid) {
		//删除文件、及文件相册的关系
		List<DiskAlbumFile> files=diskAlbumFileDao.findListByAlbumid(albumid);
		List<String> ids=new ArrayList<String>();
		if(!CollectionUtils.isEmpty(files)){
			files.forEach(file->{
				ids.add(file.getFileid());
			});
		}
		if(!CollectionUtils.isEmpty(ids)){			
			fileService.delete(userid,username,ids);
		}
		
		//删除相册
		diskAlbumDao.delete(albumid);
	}

	@Override
	public PageInfo<FileBean> findInAlbumImg(Integer page, Integer limit, String albumid) {
		return diskAlbumJdbc.findInAlbumImg(page, limit, albumid);
	}

	@Override
	public PageInfo<FileBean> findNotInAlbumImg(Integer page, Integer limit, String userid) {
		return diskAlbumJdbc.findNotInAlbumImg(page, limit, userid);
	}

	@Override
	public void setImgToAlbum(String albumid, List<String> ids) {
		if(CollectionUtils.isEmpty(ids)){
			throw new RuntimeException("您没有选择相关图片");
		}
		DiskAlbum diskalbum=diskAlbumDao.findOne(albumid);
		if(diskalbum==null){
			throw new RuntimeException("相册(ID="+albumid+")不存在");
		}
		for(int i=0;i<ids.size();i++){
			String id=ids.get(i);
			int index=i+1;
			
			DiskFile diskfile=diskFileDao.findOne(id);
			if(diskfile==null){
				throw new RuntimeException("选中的第"+index+"张图片(ID="+id+")不存在");
			}
			
			DiskAlbumFile bean=diskAlbumFileDao.findFileIsExist(id);
			if(bean!=null){
				DiskAlbum album=diskAlbumDao.findOne(bean.getAlbumid());
				throw new RuntimeException("选中的第"+index+"张图片(ID="+id+")的图片已经存在于("+album.getAlbumname()+")相册了");
			}
			//不存在则保存
			DiskAlbumFile daf=new DiskAlbumFile();
			daf.setAlbumid(albumid);
			daf.setFileid(id);
			daf.setCreatetime(new Date());
			diskAlbumFileDao.save(daf);
		}
	}

	@Override
	public void removeImgOutAlbum(String albumid,List<String> fileids) {
		if(CollectionUtils.isEmpty(fileids)){
			throw new RuntimeException("请选择图片");
		}
		for(int i=0;i<fileids.size();i++){		
			String id=fileids.get(i);
			int index=i+1;
			
			DiskFile diskfile=diskFileDao.findOne(id);
			if(diskfile==null){
				throw new RuntimeException("选中的第"+index+"张图片(ID="+id+")不存在");
			}
			
			int count=diskAlbumFileDao.findFileIsInAlbum(albumid, id);
			if(count==0){
				DiskAlbum album=diskAlbumDao.findOne(albumid);
				throw new RuntimeException("选中的第"+index+"图片(ID="+id+")已经不在("+album.getAlbumname()+")相册");
			}
			diskAlbumFileDao.deleteByFileid(fileids.get(i));
		}
	}

	@Override
	public void moveImgToAlbum(String albumid, List<String> fileids) {
		ValidateUtils.validate(albumid, "相册ID");
		if(CollectionUtils.isEmpty(fileids)){
			throw new RuntimeException("请选择图片");
		}
		for(int i=0;i<fileids.size();i++){
			int index=i+1;
			int count=diskAlbumFileDao.findFileIsInAlbum(albumid, fileids.get(i));
			if(count==0){
				//校验相册ID
				DiskAlbum diskalbum=diskAlbumDao.findOne(albumid);
				if(diskalbum==null){
					throw new RuntimeException("相册(ID="+albumid+")不存在");
				}
				DiskFile diskfile=diskFileDao.findOne(fileids.get(i));
				if(diskfile==null){
					throw new RuntimeException("选中的第"+index+"张图片(ID="+fileids.get(i)+")不存在");
				}
				
				//删除旧相册
				diskAlbumFileDao.deleteByFileid(fileids.get(i));
				//添加新相册
				DiskAlbumFile file=new DiskAlbumFile();
				file.setAlbumid(albumid);
				file.setFileid(fileids.get(i));
				file.setCreatetime(new Date());
				diskAlbumFileDao.save(file);
			}else{
				throw new RuntimeException("选中的第"+index+"张图片已经存在于该相册里面了!");
			}
		}
	}

	@Override
	public void setAlbumCover(String albumid, String fileid) {
		DiskFile file=diskFileDao.findOne(fileid);
		if(file==null){
			throw new RuntimeException("图片ID不存在");
		}
		DiskMd5 md5=diskMd5Dao.findMd5IsExist(file.getFilemd5());
		if(md5==null){
			throw new RuntimeException("图片的MD5不存在");
		}
		diskAlbumDao.updateAlbumCover(albumid, md5.getThumbnailurl());
	}

	@Override
	public void deleteAlbumCover(String albumid) {
		DiskAlbum bean=diskAlbumDao.findOne(albumid);
		if(bean==null){
			throw new RuntimeException("相册ID不存在");
		}
		bean.setCoverurl("");
	}

}

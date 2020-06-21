package com.micro.chain.handler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MergeRequest;
import com.micro.db.dao.DiskAlbumFileDao;
import com.micro.model.DiskAlbumFile;

@Component
public class MergeGlThumnailHandler extends Handler {
	@Autowired
	private DiskAlbumFileDao diskAlbumFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MergeRequest){
			MergeRequest bean=(MergeRequest) request;
			
			String albumid=bean.getAlbumid();
			String typecode=bean.getTypecode();
			String fileid=bean.getDiskfileid();
			
			if(!StringUtils.isEmpty(albumid)&&!"undefined".equals(albumid)){
				if("picture".equals(typecode)){		
					int count=diskAlbumFileDao.findFileIsInAlbum(albumid, fileid);
					if(count==0){					
						DiskAlbumFile daf=new DiskAlbumFile();
						daf.setAlbumid(albumid);
						daf.setFileid(fileid);
						daf.setCreatetime(new Date());
						diskAlbumFileDao.save(daf);
					}
				}
			}
		}else{
			throw new RuntimeException("MergeGlThumnailHandler==参数不对");
		}		
	}
}

package com.micro.chain.handler;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MoveRequest;
import com.micro.common.DateUtils;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;

@Component
public class MoveUpdateHandler extends Handler {
	@Autowired
	private DiskFileDao diskFileDao;
	
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof MoveRequest){
			MoveRequest bean=(MoveRequest) request;
			
			List<String> ids=bean.getIds();
			for(int i=0;i<ids.size();i++){
				String id=ids.get(i);
				int index=i+1;
				DiskFile df=diskFileDao.findOne(id);
				if(df==null){
					throw new RuntimeException("选择的第"+index+"条记录主键[id="+id+"]不存在");
				}
				
				if(df.getFiletype()==0){//文件夹
					DiskFile diskFile=diskFileDao.findFolderNameIsExist(bean.getUserid(),bean.getFolderid(), df.getFilename());
					if(diskFile==null){
						df.setPid(bean.getFolderid());
						diskFileDao.save(df);
					}else{
						if(!df.getPid().equals(bean.getFolderid())){//表示不是移动到本目录下							
							df.setFilename(df.getFilename()+"("+DateUtils.formatDate(new Date(),"移动于yyyy-MM-dd HH:mm:ss.S")+")");
							df.setPid(bean.getFolderid());
							diskFileDao.save(df);
						}
					}
				}else if(df.getFiletype()==1){//文件
					DiskFile diskFile=diskFileDao.findFileNameIsExist(bean.getUserid(),bean.getFolderid(), df.getFilemd5(), df.getFilename());
					if(diskFile==null){
						df.setPid(bean.getFolderid());
						diskFileDao.save(df);
					}else{
						throw new RuntimeException("目标目录已经存在相同的文件了!");
					}
				}
			}
		}else{
			throw new RuntimeException("MoveUpdateHandler==参数不对");
		}
	}
}

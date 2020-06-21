package com.micro.chain.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.micro.chain.core.ContextRequest;
import com.micro.chain.core.ContextResponse;
import com.micro.chain.core.Handler;
import com.micro.chain.param.MergeRequest;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;

/**
 * 难点说明：
 * 需求：如果一个文件夹下对应多个文件，每个文件都创建一次，会导致重复创建目录（/test/test1/1.png、/test/test1/2.png），如何解决
 * 方案一：分布式锁（不推荐）
 * 		思路：lockname=${userid}-${folderid}-${root}，folderid表示上传到的目录ID、root表示根目录，上面案例的test
 * 		问题1：内层释放锁导致的问题
 * 			整个事务尚未提交，此时线程B在这里抢到锁，但是无法读取线程A保存的值
 * 		问题2：外层释放锁导致的问题
 * 			外层释放锁时，此时整个事务也尚未提交
 * 
 * 方案二：数据库的悲观锁（不推荐）
 * 		思路：锁住全表
 * 		缺点：性能比较低
 * 
 * 方案三：重新执行
 * 		思路：①设置唯一性；②执行失败，重新再执行一遍
 * 		问题：重新执行时，查询会报错，【目前找不到原因】
 * 
 * 方案四：在最外层加锁
 * 
 * @author Administrator
 *
 */
@Component
public class MergeCreateFolderHandler extends Handler {
	//@Autowired
	//private LockContext lockContext;
	
	@Autowired
	private DiskFileDao diskFileDao;

	// relativepath: view/测试2/ControlDocumentFormatRegistry.java
	// 如果不加分布式锁：那么多个文件同时创建同名称的文件夹
	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if (request instanceof MergeRequest) {
			MergeRequest bean = (MergeRequest) request;
			String relativepath = bean.getRelativepath();
			if (!StringUtils.isEmpty(relativepath)) {
				String[] names = relativepath.split("/");
				String userid = bean.getUserid();
				String username = bean.getUsername();
				String pid = bean.getPid();
				List<DiskFile> folders = new ArrayList<>();

				try {
					for (int i = 0; i < names.length - 1; i++) {
						String name=names[i];
						DiskFile df = diskFileDao.findFolder(userid, pid, name);
						if (df == null) {
							df = new DiskFile();
							df.setFilename(name);
							df.setPid(pid);
							df.setFiletype(0);
							df.setFilesize(0);
							df.setCreateuserid(userid);
							df.setCreateusername(username);
							df.setCreatetime(new Date());
							diskFileDao.save(df);
						}
						pid = df.getId();
						folders.add(df);
					}

					// 写到下一个Handler
					bean.setPid(pid);
					bean.setFolders(folders);
					this.updateRequest(bean);

				} catch (Exception e) {
					throw new RuntimeException("MergeCreateFolderHandler=="+e.getMessage());
				}
			}
		} else {
			throw new RuntimeException("MergeCreateFolderHandler==参数不对");
		}
	}
	
}

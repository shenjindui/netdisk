package com.micro.store.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.store.service.StoreService;
import com.micro.store.utils.SpringUtils;

@Component
public class StoreContext {
	@Autowired
    private SpringUtils springUtils;
    
    @NacosValue(value="${uploadtype}",autoRefreshed=true)
    private String uploadtype;
    
    //上传
	public String upload(String group,byte[] bytes,String fileName){
       	Object obj=springUtils.getBean(uploadtype);
		if(obj!=null){
            StoreService storeService=(StoreService)obj;
        	return storeService.upload(group,bytes,fileName);
        }else{
        	throw new RuntimeException("找不到标识uploadtype=="+uploadtype);
        }
    }
	
	//下载
	public byte[] download(String group,String path){
		Object obj=springUtils.getBean(uploadtype);
		if(obj!=null){
            StoreService storeService=(StoreService)obj;
        	return storeService.download(group, path);
        }else{
        	throw new RuntimeException("找不到标识uploadtype=="+uploadtype);
        }
	}
	//下载
	public byte[] download(String path){
		Object obj=springUtils.getBean(uploadtype);
		if(obj!=null){
            StoreService storeService=(StoreService)obj;
        	return storeService.download(path);
        }else{
        	throw new RuntimeException("找不到标识uploadtype=="+uploadtype);
        }
	}
	//删除
	public void delete(String path){
		Object obj=springUtils.getBean(uploadtype);
		if(obj!=null){
            StoreService storeService=(StoreService)obj;
            storeService.delete(path);
        }else{
        	throw new RuntimeException("找不到标识uploadtype=="+uploadtype);
        }
	}
	//创建文件夹
	public void mkdir(String folders){
		Object obj=springUtils.getBean(uploadtype);
		if(obj!=null){
            StoreService storeService=(StoreService)obj;
            storeService.mkdir(folders);
        }else{
        	throw new RuntimeException("找不到标识uploadtype=="+uploadtype);
        }
	}
}

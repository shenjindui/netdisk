package com.micro.office.preview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.office.utils.SpringUtils;

@Component
public class PreviewContext {
	@Autowired
    private SpringUtils springUtils;
	
	@NacosValue(value="${converttype}",autoRefreshed=true)
    private String converttype;

	public byte[] converToPdf(byte[] bytes){
		Object obj=springUtils.getBean(converttype);
		if(obj!=null){
            PreviewService ps=(PreviewService)obj;
        	return ps.converToPdf("", bytes);
        }else{
        	throw new RuntimeException("找不到标识converttype=="+converttype);
        }
	}
}

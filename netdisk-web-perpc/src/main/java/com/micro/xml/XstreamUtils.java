package com.micro.xml;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.thoughtworks.xstream.XStream;

public class XstreamUtils {
	public static List<MenuBean> parseMenuxml(){
		try{			
			XStream stream=new XStream();
			stream.alias("menu",MenuBean.class);
			stream.alias("menus",List.class);
			
			ClassPathResource resource = new ClassPathResource("menu.xml");
			InputStream input =resource.getInputStream();
			List<MenuBean> lists=(List<MenuBean>)stream.fromXML(input);
			
			return lists;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}

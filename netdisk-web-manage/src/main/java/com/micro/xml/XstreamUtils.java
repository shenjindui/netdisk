package com.micro.xml;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.thoughtworks.xstream.XStream;

public class XstreamUtils {
	public static List<ProjectBean> parseProjectxml(){
		try{			
			XStream stream=new XStream();
			stream.alias("project",ProjectBean.class);
			stream.alias("projects",List.class);
			
			ClassPathResource resource = new ClassPathResource("project.xml");
			InputStream input =resource.getInputStream();
			List<ProjectBean> lists=(List<ProjectBean>)stream.fromXML(input);
			
			return lists;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	public static List<OpenType> parseOpenTypexml(){
		try{			
			XStream stream=new XStream();
			stream.alias("type",OpenType.class);
			stream.alias("types",List.class);
			
			ClassPathResource resource = new ClassPathResource("opentype.xml");
			InputStream input =resource.getInputStream();
			List<OpenType> lists=(List<OpenType>)stream.fromXML(input);
			
			return lists;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
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

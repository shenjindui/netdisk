package com.micro.disk.user.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TreeJson implements Serializable{
	private String id;
	private String label;
	private String type;//org,position,user
	private boolean disabled=false;//true表示checkbox可以禁用，false表示启用
	private List<TreeJson> children=new ArrayList<TreeJson>();
}

package com.micro.disk.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FolderTree implements Serializable{
	private String id;
	private String label;
	private boolean isLeaf;
	private List<FolderTree> children=new ArrayList<FolderTree>();
}

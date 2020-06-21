package com.micro.disk.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TypeTree implements Serializable{
	private String code;
	private String label;
	private List<TypeTree> children=new ArrayList<TypeTree>();
}

package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class AnalyLogBean implements Serializable{
	private String name;
	private String analytime;
	private String analycondition;
	private Long requestcount;
	private Long successcount;
	private Long errorcount;
	private Double avgtime;
	private Long maxtime;
	private Long mintime;
}

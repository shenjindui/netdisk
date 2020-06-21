package com.micro.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Table(name="disk_share_save",
indexes = {
	@Index(columnList = "shareid"),
	@Index(columnList = "userid"),
}
)
@Entity
@Data
public class DiskShareSave {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="shareid",columnDefinition="VARCHAR(50)")
	private String shareid;
	
	private String userid;
	private String username;
	private Date createtime;
}

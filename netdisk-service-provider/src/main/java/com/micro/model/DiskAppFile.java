package com.micro.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
@Table(name="disk_app_registry_file",
	indexes = {
		@Index(columnList = "appid"),
		@Index(columnList = "businessid"),
		@Index(columnList = "businesstype"),
		@Index(columnList = "typecode"),
		@Index(columnList = "delstatus"),
		@Index(columnList = "createuserid")
	}
)
@Entity
@Data
public class DiskAppFile implements Serializable {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="appid",columnDefinition="VARCHAR(50)")
	private String appid;
	
	@Column(name="businessid",columnDefinition="VARCHAR(20)")
	private String businessid;
	
	@Column(name="businesstype",columnDefinition="VARCHAR(20)")
	private String businesstype;
	
	@Column(name="filename",columnDefinition="VARCHAR(255)")
	private String filename;
	
	private long filesize;
	
	@Column(name="filesuffix",columnDefinition="VARCHAR(20)")
	private String filesuffix;
	
	@Column(name="typecode",columnDefinition="VARCHAR(20)")
	private String typecode;
	
	@Column(name="filemd5",columnDefinition="VARCHAR(200)")
	private String filemd5;
	
	private Integer delstatus;//0正常，1删除
	
	@Column(name="createuserid",columnDefinition="VARCHAR(20)")
	private String createuserid;
	
	@Column(name="createusername",columnDefinition="VARCHAR(20)")
	private String createusername;
	
	private Date createtime;
	
	private Integer isbreak;//0正常,1为损坏
}

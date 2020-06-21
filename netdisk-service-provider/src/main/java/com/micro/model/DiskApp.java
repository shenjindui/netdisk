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
@Table(name="disk_app_registry",
	indexes = {
	}
)
@Entity
@Data
public class DiskApp implements Serializable {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	private String appname;
	private String appdesc;
	private String createuserid;
	private String createusername;
	private Date createtime;
	private Integer delstatus;
}

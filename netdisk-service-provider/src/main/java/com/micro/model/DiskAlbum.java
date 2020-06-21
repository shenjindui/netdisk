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

/**
 * 相册
 * @author Administrator
 *
 */
@Table(
	name="disk_album",
	indexes = {
		@Index(columnList = "createuserid")
	}
)
@Entity
@Data
public class DiskAlbum {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="albumname",columnDefinition="VARCHAR(255)")
	private String albumname;
	
	@Column(name="albumdesc",columnDefinition="VARCHAR(255)")
	private String albumdesc;
	
	@Column(name="createuserid",columnDefinition="VARCHAR(50)")
	private String createuserid;
	
	@Column(name="createusername",columnDefinition="VARCHAR(50)")
	private String createusername;
	
	private Date createtime;
	
	@Column(name="coverurl",columnDefinition="VARCHAR(255)")
	private String coverurl;//封面
}

package com.micro.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
@Table(
	name="disk_album_file",
	uniqueConstraints={@UniqueConstraint(columnNames={"fileid"})},   //唯一约束
	indexes = {
		@Index(columnList = "albumid"),
		@Index(columnList = "fileid")
	}
)
@Entity
@Data
public class DiskAlbumFile implements Serializable {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="albumid",columnDefinition="VARCHAR(50)")
	private String albumid;
	
	@Column(name="fileid",columnDefinition="VARCHAR(255)")
	private String fileid;
	
	private Date createtime;
}

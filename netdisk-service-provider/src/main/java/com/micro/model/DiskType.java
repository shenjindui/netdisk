package com.micro.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import lombok.Data;


/**
 * 文件分类（比如：文档、图片、视频、音乐、其他）
 * @author Administrator
 *
 */
@Table(
	name="disk_type",
	uniqueConstraints={
		@UniqueConstraint(columnNames={"code"})
	},
	indexes = {
		@Index(columnList = "code")
	}
)
@Entity
@Data
public class DiskType implements Serializable{
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="code",columnDefinition="VARCHAR(50)")
	private String code;
	
	@Column(name="name",columnDefinition="VARCHAR(50)")
	private String name;
}

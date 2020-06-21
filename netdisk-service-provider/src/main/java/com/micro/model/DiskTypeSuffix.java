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
 * 文件分类（比如：文档、图片、视频、音乐、其他）---每种分类对应的具体格式
 * @author Administrator
 *
 */
@Table(
	name="disk_type_suffix",
	uniqueConstraints={
		@UniqueConstraint(columnNames={"suffix"})
	},
	indexes = {
		@Index(columnList = "typecode"),
		@Index(columnList = "suffix")
	}
)
@Entity
@Data
public class DiskTypeSuffix implements Serializable{
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="typecode",columnDefinition="VARCHAR(50)")
	private String typecode;
	
	@Column(name="name",columnDefinition="VARCHAR(50)")
	private String name;
	
	@Column(name="icon",columnDefinition="LONGTEXT")
	private String icon;//32*32
	
	@Column(name="iconbig",columnDefinition="LONGTEXT")
	private String iconbig;//128*128
	
	@Column(name="suffix",columnDefinition="VARCHAR(50)")
	private String suffix;
	
	//@Column(name="opentype",columnDefinition="VARCHAR(50)")
	//private String opentype;//打开方式（image,pdf,txt,java,video,audio,office）
}

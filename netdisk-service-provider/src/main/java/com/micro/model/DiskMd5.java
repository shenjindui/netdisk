package com.micro.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Table(
	name="disk_md5",
	uniqueConstraints={@UniqueConstraint(columnNames={"md5"})},   //唯一约束
	indexes = {@Index(columnList = "md5")}                        //为字段roleId加上索引
)
@Entity
@Data
public class DiskMd5 implements Serializable {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="md5",columnDefinition="VARCHAR(200)")
	private String md5;
	
	private long filenum;//切块数量
	
	private String typecode;
	private String filename;
	private String filesuffix;
	private long filesize;
	
//////////////////////////////////图片的扩展属性///////////////////////////////////////////
	@Column(name="thumbnailurl",columnDefinition="VARCHAR(200)")
	private String thumbnailurl;//图片属性：缩略图
	
	@Column(name="imgsize",columnDefinition="VARCHAR(50)")
	private String imgsize;//图片属性：尺寸
	
}

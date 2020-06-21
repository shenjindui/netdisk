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

@Table(
	name="disk_type_suffix_operate",
	uniqueConstraints={
		@UniqueConstraint(columnNames={"suffix","componentcode"})
	},
	indexes = {
		@Index(columnList = "suffix")
	}
)
@Entity
@Data
public class DiskTypeSuffixOperate implements Serializable{
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="suffix",columnDefinition="VARCHAR(50)")
	private String suffix;
	
	@Column(name="componentcode",columnDefinition="VARCHAR(50)")
	private String componentcode;//组件名称
}

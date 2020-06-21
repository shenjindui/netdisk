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
	name="disk_user_capacity",
	uniqueConstraints={
		@UniqueConstraint(columnNames={"userid"})
	},
	indexes = {
		@Index(columnList = "userid")
	}
)
@Entity
@Data
public class DiskUserCapacity implements Serializable{
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="userid",columnDefinition="VARCHAR(50)")
	private String userid;
	
	private Long totalcapacity;//总容量
	private Long usedcapacity;//已用容量
	
	
}

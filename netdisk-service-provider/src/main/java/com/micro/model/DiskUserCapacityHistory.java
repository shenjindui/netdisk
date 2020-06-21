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

@Table(
	name="disk_user_capacity_history",
	indexes = {
		@Index(columnList = "userid")
	}
)
@Entity
@Data
public class DiskUserCapacityHistory {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	private String userid;//容量所属用户id，比如：分配容量的时候，操作人和容量的所属人不是同一个
	private String remark;
	private String createuserid;//操作人
	private String createusername;
	private Date createtime;
	private int type;//0减少，1新增
	private Long capacity;//本次减少/新增的容量
	private Long leftcapacity;//总剩余容量
	
}

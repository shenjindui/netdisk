package com.micro.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Table(name="log_project")
@Entity
@Data
public class LogProject {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="projectname",columnDefinition="VARCHAR(50)")
	private String projectname;
	
	@Column(name="projectdesc",columnDefinition="VARCHAR(50)")
	private String projectdesc;
}

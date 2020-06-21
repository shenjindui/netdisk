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
	name="disk_file_edit",
	indexes = {
		@Index(columnList = "fileid")
	}
)
@Entity
@Data
public class DiskFileEdit {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="fileid",columnDefinition="VARCHAR(50)")
	private String fileid;
	
	@Column(name="edituserid",columnDefinition="VARCHAR(50)")
	private String edituserid;
	
	@Column(name="editusername",columnDefinition="VARCHAR(50)")
	private String editusername;
	
	private Date edittime;
	
	@Column(name="filemd5",columnDefinition="VARCHAR(50)")
	private String filemd5;
	
	@Column(name="prevfilemd5",columnDefinition="VARCHAR(50)")
	private String prevfilemd5;
	//难点：如何像代码托管中心一样，识别哪里修改，并且标准高亮
}

package com.micro.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
@Table(name="disk_share",
	indexes = {
		@Index(columnList = "shareuserid"),
		@Index(columnList = "type"),
		@Index(columnList = "sharetype"),
		@Index(columnList = "effect"),
		@Index(columnList = "code"),
		@Index(columnList = "status")
	}
)
@Entity
@Data
public class DiskShare implements Serializable {
	@Id 
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid") 
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="shareuserid",columnDefinition="VARCHAR(50)")
	private String shareuserid;
	
	@Column(name="shareusername",columnDefinition="VARCHAR(50)")
	private String shareusername;
	
	@Column(name="title",columnDefinition="LONGTEXT")
	private String title;
	
	private Date sharetime;
	
	private Integer type;//0私密链接分享，1好友分享
	
	private Integer sharetype;//【属于私密分享】0有提取码，1无提取码
	
	private Integer effect;//【属于私密分享】0永久，7表示7天，1表示1天
	
	@Column(name="url",columnDefinition="VARCHAR(200)")
	private String url;//【属于私密分享】链接地址
	
	@Column(name="code",columnDefinition="VARCHAR(20)")
	private String code;//【属于私密分享】提取码
	
	private Date endtime;//【属于私密分享】过期时间（effect!=0）
	
	
	private Integer status;//0正常，1已失效，2已撤销
	private Integer savecount;//保存次数（具体明细跟mongodb日志库关联）
}

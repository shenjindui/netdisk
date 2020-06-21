package com.micro.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description= "切块参数")
@Data
public class ChunkPojo {
	@ApiModelProperty(value = "前端生成的uuid",required=true)
    private String uuid;
	
	@ApiModelProperty(value = "前端生成的文件ID",required=true)
    private String id;
	
	@ApiModelProperty(value = "文件名称",required=true)
    private String name;
	
	@ApiModelProperty(value = "文件大小",required=true)
    private Long size;
	
	@ApiModelProperty(value = "切块序号",required=true)
    private Integer chunk;
	
	@ApiModelProperty(value = "切块数量",required=true)
    private Integer chunks;
}

package com.micro.disk.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class Chunk implements Serializable{
    private String uuid; //uuid
    
    //WebUloader.js自动携带的参数
    private String id; //前端生成的文件ID
    private String name; //文件名称
    private Long size; //文件大小
    private Integer chunk; //切块序号
    private Integer chunks; //切块数量
    
    private byte[] bytes;
    private String userid;
    private String username;
}

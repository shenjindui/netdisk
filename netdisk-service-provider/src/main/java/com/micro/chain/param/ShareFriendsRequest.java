package com.micro.chain.param;

import java.util.List;

import com.micro.chain.core.ContextRequest;
import com.micro.disk.bean.ShareFriendsBean;

import lombok.Data;

@Data
public class ShareFriendsRequest extends ContextRequest{
	private List<String> ids;
	private List<ShareFriendsBean> friends;
	private String title;
	private String userid;
	private String username;
	private Integer type;
	
	//补充
	private String shareid;
}

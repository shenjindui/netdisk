package com.micro.common;

public class NoticeConstant {
	public static String NOTICE_SHARE_FRIENDS_CREATE="notice_share_friends_create";//好友分享（需要管）
	public static String NOTICE_SHARE_FRIENDS_CANCEL="notice_share_friends_cancel";//好友取消分享（需要管）
	
	public static String NOTICE_SHARE_SECRET_OVERDUE="notice_share_secret_overdue";//私密链接过期提醒（监听redis）
	public static String NOTICE_RUBBISH_OVERDUE="notice_rubbish_overdue";//回收站到30天提醒（监听redis）
	
	public static String NOTICE_CAPACITY_ADD="notice_capacity_add";//新增容量、开通容量（暂时不管）
	public static String NOTICE_CAPACITY_NONE="notice_capacity_none";//容量不足（暂时不管）
	
	/**
	 * 获取类型名称
	 * @param type
	 * @return
	 */
	public static String getTypeName(String type){
		switch (type) {
			case "notice_share_friends_create":
				return "好友分享";
	
			case "notice_share_friends_cancel":
				return "好友取消分享";
			
			case "notice_share_secret_overdue":
				return "私密链接分享到期";
				
			case "notice_rubbish_overdue":
				return "回收站文件到期";
				
			case "notice_capacity_add":
				return "开通容量";
				
			case "notice_capacity_none":
				return "容量不足";
				
		}
		return null;
	}
	/**
	 * 生成通知模板
	 * @return
	 */
	public static String getNoticeTemplate(){
		
		return null;
	}
}

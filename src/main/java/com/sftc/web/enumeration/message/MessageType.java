package com.sftc.web.enumeration.message;

/**
 * sftc_message 
 *  message_type消息类型 
 * @author wencai
 *
 */
public enum MessageType {

	RECEIVE_ADDRESS(0,"RECEIVE_ADDRESS"),//接收到好友地址的通知（作为寄件人）
	RECEIVE_EXPRESS(1,"RECEIVE_EXPRESS");//接收到好友包裹的通知（作为收件人）
	
	private int key;
	private String value;
	
	private MessageType(int key,String value) {
		this.key = key;
		this.value = value;
		
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}

package com.sftc.web.enumeration.message;

/**
 * sftc_message 
 *  message_type消息类型 
 * @author wencai
 *
 */
public enum MessageType {

	RECEIVE_ADDRESS("RECEIVE_ADDRESS","接收到好友地址的通知（作为寄件人）"),
	RECEIVE_EXPRESS("RECEIVE_EXPRESS","接收到好友包裹的通知（作为收件人）");
	
	private String key;
	private String value;
	
	private MessageType(String key,String value) {
		this.key = key;
		this.value = value;
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}

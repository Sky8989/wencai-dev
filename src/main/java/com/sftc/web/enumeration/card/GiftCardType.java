package com.sftc.web.enumeration.card;
/**
 * sftc_gift_card 
 * type礼品卡类型
 * @author wencai
 */
public enum GiftCardType {

	FESTIVAL(0,"FESTIVAL"); //节日必备
	
	private int key;
	private String value;
	
	private GiftCardType(int key,String value) {
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

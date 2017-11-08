package com.sftc.web.enumeration.card;

public enum GiftCardType {

	FESTIVAL("FESTIVAL","节日必备");
	
	private String key;
	private String value;
	
	private GiftCardType(String key,String value) {
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

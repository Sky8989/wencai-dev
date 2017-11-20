package com.sftc.web.enumeration.address;


/**
 * sftc_address_book 
 *  address_type地址类型
 * @author wencai
 *
 */
public enum AddressType {
	
	address_history(0,"address_history"),//历史地址
	address_book(1,"address_book");	//地址簿
   
	private int key;
	private String value;
	
	private AddressType(int key,String value) {
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

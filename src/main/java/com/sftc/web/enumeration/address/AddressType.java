package com.sftc.web.enumeration.address;


/**
 * sftc_address_book 
 *  address_type地址类型
 * @author wencai
 *
 */
public enum AddressType {
	
	ADDRESS_HISTORY("address_history","历史地址"),
	ADDRESS_BOOK("address_book","地址簿");
   
    
	private String key;
	private String value;
	
	private AddressType(String key,String value) {
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

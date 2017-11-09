package com.sftc.web.enumeration.address;


/**
 * sftc_address_book  
 *  address_book_type地址簿类型
 * @author wencai
 * 
 */
public enum AddressBookType {
	
	SENDER("sender","寄件人地址簿"),
	SHIP("ship","收件人地址簿");
   
    
	private String key;
	private String value;
	
	private AddressBookType(String key,String value) {
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

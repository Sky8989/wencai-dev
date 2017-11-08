package com.sftc.web.enumeration.address;



public enum AddressBookType {
	
	SENDER("SENDER","寄件人地址簿"),
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

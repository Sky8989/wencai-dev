package com.sftc.web.enumeration.address;



public enum AddressType {
	
	ADDRESS_HISTORY("ADDRESS_HISTORY","历史地址"),
	ADDRESS_BOOK("ADDRESS_BOOK","地址簿");
   
    
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

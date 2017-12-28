package com.sftc.web.enumeration.address;


/**
 * c_address_book
 *  address_type地址类型
 * @author wencai
 *
 */
public enum AddressType {
	
	address_history("address_history","历史地址"),
	address_book("address_book","地址簿");
   
    
	private String name;
	private String value;
	
	private AddressType(String name,String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	
	
	
}

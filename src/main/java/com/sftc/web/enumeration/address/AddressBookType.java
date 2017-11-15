package com.sftc.web.enumeration.address;


/**
 * sftc_address_book  
 *  address_book_type地址簿类型
 * @author wencai
 * 
 */
public enum AddressBookType {
	
	sender("sender","寄件人地址簿"),
	ship("ship","收件人地址簿");
   
    
	private String name;
	private String value;
	
	private AddressBookType(String name,String value) {
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

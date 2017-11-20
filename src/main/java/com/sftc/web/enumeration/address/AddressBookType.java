package com.sftc.web.enumeration.address;


/**
 * sftc_address_book  
 *  address_book_type地址簿类型
 * @author wencai
 * 
 */
public enum AddressBookType {
	
	sender(0,"sender"),	//寄件人地址簿
	ship(1,"ship");		//收件人地址簿
   
    private int key;
    private String value;
    
	private AddressBookType(int key, String value) {
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

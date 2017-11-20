package com.sftc.web.enumeration.express;


/**
 * sftc_order_express 
 *  package_type包裹类型
 * @author wencai
 *
 */
public enum PackageType {
	
	SMALl_PACKAGE(0,"0"),//小包裹
	CENTRN_PACKAGE(1,"1"),//中包裹
	BIG_PACKAGE(2,"2"),//大包裹
	HUGE_PACKAGE(3,"3");//超大包裹
    
	private int key;
	private String value;
	
	private PackageType(int key,String value) {
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

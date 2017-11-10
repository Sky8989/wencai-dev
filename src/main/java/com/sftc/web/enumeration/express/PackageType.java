package com.sftc.web.enumeration.express;



public enum PackageType {
	
	SMALl_PACKAGE("0","小包裹"),
	CENTRN_PACKAGE("1","中包裹"),
	BIG_PACKAGE("2","大包裹"),
	HUGE_PACKAGE("3","超大包裹");

    
	private String key;
	private String value;
	
	private PackageType(String key,String value) {
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

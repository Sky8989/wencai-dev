package com.sftc.web.enumeration.express;



public enum PackageType {
	
	SMALl_PACKAGE("SMALl_PACKAGE","小包裹"),
	CENTRN_PACKAGE("CENTRN_PACKAGE","中包裹"),
	BIG_PACKAGE("BIG_PACKAGE","大包裹");
   
    
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

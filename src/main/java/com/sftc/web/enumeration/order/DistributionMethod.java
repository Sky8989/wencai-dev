package com.sftc.web.enumeration.order;

/**
 * c_order
 *  distribution_method配送方式
 * @author wencai
 *
 */
public enum DistributionMethod {


	JISUDA("JISUDA","同城专送"),
	KUAISUDA("KUAISUDA","同城专送 快速达");

	private String key;
	private String value;
	
	private DistributionMethod(String key,String value) {
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

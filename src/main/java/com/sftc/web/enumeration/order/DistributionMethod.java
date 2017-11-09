package com.sftc.web.enumeration.order;

/**
 * sftc_order
 *  distribution_method配送方式
 * @author wencai
 *
 */
public enum DistributionMethod {


	JISUDA("JISUDA","同城专送"),
	SF_MORROW("1","顺丰次日"),
	SF_EOD("2","顺丰隔日"),
	SF_MORROW_MORNING("5","同城专送");
	
	
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

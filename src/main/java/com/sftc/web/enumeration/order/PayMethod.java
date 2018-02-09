package com.sftc.web.enumeration.order;
/**
 * sftc_order
 *  pay_method支付方式
 * @author wencai
 *
 */
public enum PayMethod {

	FREIGHT_PREPAID("FREIGHT_PREPAID","寄付"),
	FREIGHT_COLLECT("FREIGHT_COLLECT","到付");
	
	private String key;
	private String value;
	
	private PayMethod(String key,String value) {
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

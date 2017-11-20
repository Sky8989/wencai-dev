package com.sftc.web.enumeration.order;
/**
 * sftc_order
 *  pay_method支付方式
 * @author wencai
 *
 */
public enum PayMethod {

	FREIGHT_PREPAID(0,"FREIGHT_PREPAID"),//寄付
	FREIGHT_COLLECT(1,"FREIGHT_COLLECT");//到付
	
	private int key;
	private String value;
	
	private PayMethod(int key,String value) {
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

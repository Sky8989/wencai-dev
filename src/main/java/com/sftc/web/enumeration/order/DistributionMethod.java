package com.sftc.web.enumeration.order;

/**
 * sftc_order
 *  distribution_method配送方式
 * @author wencai
 *
 */
public enum DistributionMethod {


	JISUDA(0,"JISUDA"),//同城专送
	SF_MORROW(1,"顺丰次日"),
	SF_EOD(2,"顺丰隔日"),
	KUAISUDA(3,"KUAISUDA"),//同城专送 快速达
	SF_MORROW_MORNING(4,"大网 顺丰次晨");
	
	
	private int key;
	private String value;
	
	private DistributionMethod(int key,String value) {
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

package com.sftc.web.enumeration.order;

/**
 * sftc_order
 * @author wencai
 *
 */
public enum RegionType {

	//region_type区域类型
		REGION_SAME(0,"REGION_SAME"),//同城订单
		REGION_NATION(1,"REGION_NATION");//大网订单
	
	private int key;
	private String value;
	
	private RegionType(int key,String value) {
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

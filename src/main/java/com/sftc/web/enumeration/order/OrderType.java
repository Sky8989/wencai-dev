package com.sftc.web.enumeration.order;


/**
 * sftc_order
 * @author wencai
 *
 */
public enum OrderType {
	
	//order_type订单类型
	ORDER_BASIS("ORDER_BASIS","普通订单"),
	ORDER_MYSTERY("ORDER_MYSTERY","好友订单");

	private String key;
	private String value;
	
	private OrderType(String key,String value) {
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

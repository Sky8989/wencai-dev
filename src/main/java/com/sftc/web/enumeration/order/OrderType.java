package com.sftc.web.enumeration.order;


/**
 * sftc_order
 * @author wencai
 *
 */
public enum OrderType {
	
	//order_type订单类型
	ORDER_BASIS(0,"ORDER_BASIS"),//普通订单
	ORDER_MYSTERY(1,"ORDER_MYSTERY");//好友订单

	private int key;
	private String value;
	
	private OrderType(int key,String value) {
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

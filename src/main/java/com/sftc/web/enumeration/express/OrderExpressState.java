package com.sftc.web.enumeration.express;

/**
 * sftc_order_express	
 *  state订单状态
 * @author wencai
 *
 */

public enum OrderExpressState {
	
	INIT("INIT","下单"),
	PAYING("PAYING","支付中"),
	WAIT_HAND_OVER("WAIT_HAND_OVER","待揽件"),
	DELIVERING("DELIVERING","派送中"),
	FINISHED("FINISHED","已完成"),
	ABNORMAL("ABNORMAL","不正常的"),
	CANCELED("CANCELED","取消单"),
	WAIT_REFUND("WAIT_REFUND","等待退款"),
	REFUNDING("REFUNDING","退款中"),
	REFUNDED("REFUNDED","已退款"),
	OVERTIME("OVERTIME","填写超时、付款超时");
   
    
	private String key;
	private String value;
	
	private OrderExpressState(String key,String value) {
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

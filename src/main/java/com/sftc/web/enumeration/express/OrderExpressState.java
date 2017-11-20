package com.sftc.web.enumeration.express;

/**
 * sftc_order_express	
 *  state订单状态
 * @author wencai
 *
 */

public enum OrderExpressState {
	
	WAIT_FILL(0,"WAIT_FILL"),//待好友填写
	ALREADY_FILL(1,"ALREADY_FILL"),//好友已填写
	INIT(2,"INIT"),//下单
	PAYING(3,"PAYING"),//支付中
	WAIT_HAND_OVER(4,"WAIT_HAND_OVER"),//待揽件
	DELIVERING(5,"DELIVERING"),//派送中
	FINISHED(6,"FINISHED"),//已完成
	ABNORMAL(7,"ABNORMAL"),//不正常的
	CANCELED(8,"CANCELED"),//取消订单
	WAIT_REFUND(9,"WAIT_REFUND"),//等待退款
	REFUNDING(10,"REFUNDING"),//退款中
	REFUNDED(11,"REFUNDED"),//已退款
	OVERTIME(12,"OVERTIME");//填写超时、付款超时
    
	private int key;
	private String value;
	
	private OrderExpressState(int key,String value) {
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

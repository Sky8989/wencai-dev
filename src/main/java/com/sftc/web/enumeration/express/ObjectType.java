package com.sftc.web.enumeration.express;


/**
 * sftc_order_express 
 * 	object_type物品类型
 * @author wencai
 *
 */
public enum ObjectType {
	
	FILE(0,"FILE"), //文件
	ELECTRONICS(1,"ELECTRONICS"),//电子产品
	DRESS_SHOES(2,"DRESS_SHOES"),//服装鞋帽
	AUTOPARTS(3,"AUTOPARTS"),//汽车配件
	FRESH(4,"FRESH"),//生鲜蔬果
	FLOWER(5,"FLOWER"),//鲜花
	CAKE(6,"CAKE"),//蛋糕
	CATERING_TAKEOUT(7,"CATERING_TAKEOUT"),//餐饮
	OTHERS(8,"OTHERS");//其他
   
    
	private int key;
	private String value;
	
	private ObjectType(int key,String value) {
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

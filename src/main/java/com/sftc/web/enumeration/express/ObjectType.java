package com.sftc.web.enumeration.express;


/**
 * c_order_express
 * 	object_type物品类型
 * @author wencai
 *
 */
public enum ObjectType {
	
	FILE("FILE","文件"),
	ELECTRONICS("ELECTRONICS","电子产品"),
	DRESS_SHOES("DRESS_SHOES","服装鞋帽"),
	AUTOPARTS("AUTOPARTS","汽车配件"),
	FRESH("FRESH","生鲜蔬果"),
	FLOWER("FLOWER","鲜花"),
	CAKE("CAKE","蛋糕"),
	CATERING_TAKEOUT("CATERING_TAKEOUT","餐饮"),
	OTHERS("OTHERS","其他");
   
    
	private String key;
	private String value;
	
	private ObjectType(String key,String value) {
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

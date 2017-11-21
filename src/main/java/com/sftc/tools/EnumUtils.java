package com.sftc.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sftc.web.enumeration.express.PackageType;
import com.sftc.web.enumeration.order.DistributionMethod;
import com.sftc.web.model.entity.AddressBook;

/**
 * 
 * @author wencai
 *
 */
public class EnumUtils {
	
	/**
	 *  通用枚举处理
	 * @param str 前台传过来的String类型的枚举值
	 * @param clazz str值对应的枚举类
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException 
	 * @throws NoSuchFieldException 
	 */
	public static <T> Object enumValueOf(String str,Class<T> clazz) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchFieldException{
		String name = clazz.getName();
		Object obj = clazz.getClass();
		Method method = clazz.getMethod("valueOf", String.class);
		Object obj1 =  method.invoke(obj, str);
		Field field  = clazz.getField(str);
		
		return field.get(obj1);
	}
	
	/**
	 * 包裹类型枚举特殊处理
	 * @param str
	 * @return
	 */
	public static PackageType packageTypeValueOf(String str){
		PackageType type = null;
		switch (str) {
		case "0":
			type = PackageType.valueOf("SMALl_PACKAGE").SMALl_PACKAGE;
			break;
		case "1":
			type = PackageType.valueOf("CENTRN_PACKAGE").CENTRN_PACKAGE;
			break;
		case "2":
			type = PackageType.valueOf("BIG_PACKAGE").BIG_PACKAGE;
			break;
		case "3":
			type = PackageType.valueOf("HUGE_PACKAGE").HUGE_PACKAGE;
			break;
		default:
			
			break;
			
		}
		return type;
		
	}
	
	/**
	 * 配送方式处理
	 * @param str
	 * @return
	 */
	public static DistributionMethod  distributionValueOf(String str){
		DistributionMethod distribution = null;
		switch (str) {
		case "JISUDA":
			distribution = DistributionMethod.valueOf("JISUDA").JISUDA;
			break;
		case "KUAISUDA":
			distribution = DistributionMethod.valueOf("KUAISUDA").KUAISUDA;
			break;
		default:
			break;
		}
		return distribution;
				
	}

}

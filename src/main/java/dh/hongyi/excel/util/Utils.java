package dh.hongyi.excel.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import dh.hongyi.excel.exception.GenericBusinessException;

public class Utils {

	/**
	 * 将字符串转换成数字对象
	 * @param field
	 * @param fieldVal
	 * @param fieldValType
	 * @return
	 * @throws GenericBusinessException
	 */
	public static Number strTransToNumber(Field field,String fieldVal,Class<?> fieldValType) throws GenericBusinessException{
		if (!Number.class.isAssignableFrom(fieldValType) && (!checkStrIsNumber(fieldVal))) {
			throw new GenericBusinessException(fieldVal+"值的类型为"+fieldValType.getName()+"与"+field.getType().getName()+"字段类型不匹配!");
		} 
		if (BigDecimal.class.isAssignableFrom(fieldValType)) {
			return strToBigdecimal(fieldVal);
		} else if (Long.class.isAssignableFrom(fieldValType)) {
			return strToLong(fieldVal);
		} else if (Double.class.isAssignableFrom(fieldValType)) {
			return strToDouble(fieldVal);
		} else if (Float.class.isAssignableFrom(fieldValType)) {
			return strToFloat(fieldVal);
		} else {
			return new BigDecimal(fieldVal);
		}
	}
	/**
     * 字符串转Long类型
     * @param str 参数值
     * @return 如果参数值为空则返回0L,否则返回转换后的Long值
     */
    public static Long strToLong(String str){
    	return isEmpty(str) ? 0L : Long.valueOf(str);
    }
    /**
     * 字符串转Double类型
     * @param str 参数值
     * @return 如果参数值为空则返回0D,否则返回转换后的Double值
     */
    public static Double strToDouble(String str){
    	return isEmpty(str) ? 0D : Double.valueOf(str);
    }
    /**
     * 字符串转Float类型
     * @param str 参数值
     * @return 如果参数值为空则返回0F,否则返回转换后的Float值
     */
    public static Float strToFloat(String str){
    	return isEmpty(str) ? 0F : Float.valueOf(str);
    }
    /**
     * 字符串转BigDecimal类型
     * @param str 参数值
     * @return 如果参数值为空则返回值为0的BigDecimal对象,否则返回转换后的BigDecimal值
     */
    public static BigDecimal strToBigdecimal(String str){
    	return isEmpty(str) ? BigDecimal.ZERO : new BigDecimal(str);
    }
    
    public static String bigdecimalToStr(BigDecimal bigDecimal){
    	return bigDecimal == null ? "" : bigDecimal.toString();
    }
    
    /**
	 * 通过正则判断字符串是否为数字类型
	 * @param str 参数值
	 * @return 参数值为空或者不符合正则表达式返回true,否则返回false
	 */
	public static boolean checkStrIsNumber(String str){
		if (StringUtils.isEmpty(str))  return true;
		try {
			Float.parseFloat(str);
			return true;
		} catch (NumberFormatException e) {
			 return false;
		}
	}
	
	/**
	 * 判断参数值是否为空
	 * @param str 参数值
	 * @return 为空或者为null字符串返回true,否则返回false
	 */
	public static boolean isEmpty(String str){
		return StringUtils.isEmpty(str) || "null".equalsIgnoreCase(str) ? true : false;
	}
}

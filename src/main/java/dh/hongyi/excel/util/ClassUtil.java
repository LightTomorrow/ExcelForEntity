package dh.hongyi.excel.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

import dh.hongyi.excel.exception.GenericBusinessException;

public class ClassUtil {
	
	public static Object createInstance(Class<?> clazz) throws Exception{
		return clazz.newInstance();
	}

	@SuppressWarnings("unchecked")
	public static <T> T createInstance(Class<?> clazz,Class<T> requireType) throws Exception{
		return (T) clazz.newInstance();
	}
	
	public static Field[] getClassFields(Class<?> clazz){
		return getClassFields(clazz,false);
	}
	
	public static Field[] getClassFields(Class<?> clazz,boolean isPublicField){
		if (isPublicField) {
			return clazz.getFields();
		}
		return clazz.getDeclaredFields();
	}

	/**
	 * 设置字段可以访问
	 * @param field
	 */
	public static void setAccessible(Field field) {
		if(!field.isAccessible()) {
			field.setAccessible(true);
		}
	}
	
	/**
	 * 获取字段带有泛型的实际class对象
	 * @param field
	 * @return
	 * @throws GenericBusinessException 字段不匹配{@link java.lang.reflect.ParameterizedType}类型
	 */
	public static Class<?> getActualClassForField(Field field) throws GenericBusinessException{
		if (field.getGenericType() instanceof ParameterizedType) {
			ParameterizedType genericType = (ParameterizedType) field.getGenericType();
			if (genericType != null && genericType.getActualTypeArguments() != null) {
				if(genericType.getActualTypeArguments()[0] instanceof Class) {
					return ((Class<?>) genericType.getActualTypeArguments()[0]);
				}
			}
		}
		throw new GenericBusinessException(field.getName()+" not is parameterizedType!");
	}
	
	/**
	 * 根据类的全路径加载此类
	 * @param classFullName
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(String classFullName) throws ClassNotFoundException{
		return Thread.currentThread().getContextClassLoader().loadClass(classFullName);
	}
	
	/**
	 * 判断字段是否使用了public,static,final关键字修饰
	 * @param field 
	 * @return 如果使用了public,static,final关键字修饰返回true,否则返回false
	 */
	public static boolean isPublicStaticFinal(Field field){
		return (Modifier.isPublic(field.getModifiers()) || 
				Modifier.isStatic(field.getModifiers()) || 
				Modifier.isFinal(field.getModifiers())
				);
	}
	
	/**
	 * 判断annotatedElement对象是否使用指定注解类型
	 * @param annotatedElement
	 * @param annotationClass
	 * @return 是则返回true,否则返回false
	 */
	public static <A extends Annotation> boolean hasAnnotation(AnnotatedElement annotatedElement,Class<A> annotationClass) {
		return annotatedElement.isAnnotationPresent(annotationClass);
	}
	/**
	 * 判断字段是否为集合类型
	 * @param field 
	 * @return 如果是则返回true,否则返回false
	 */
	public static boolean isCollection(Field field) {
		return Collection.class.isAssignableFrom(field.getType());
	}
	/**
	 * 判断字段是否为Map类型
	 * @param field 
	 * @return 如果是则返回true,否则返回false
	 */
	public static boolean isMap(Field field) {
		return Map.class.isAssignableFrom(field.getType());
	}
}

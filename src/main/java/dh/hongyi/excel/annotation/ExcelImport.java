package dh.hongyi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel导入字段标记
 * @author HelloLight
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelImport {
	
	/**
	 * 标记Excel标题名称
	 * @return
	 */
	 String name();
	
    /**
     * 导入的时间格式,以这个是否为空来判断是否需要格式化日期
     */
    String dateFormat() default "";
    
    /**
     * 是否需要字段值必填,默认为不需要
     * @return
     */
    boolean require() default false;
    
    /**
     * 标记字段是否为枚举值</br>
     * @see {@link #parentEnumCode()}
     * @return 默认为否,设置为true则需要指定对应的枚举值父编码
     */
    boolean isEnum() default false;
    
    /**
	 * 枚举父编码-如果是枚举值必须传入父编码,</br>
	 * 根据父编码去枚举类映射的数据库表中查询对应的枚举值
	 * @return
	 */
	String parentEnumCode() default "";

}

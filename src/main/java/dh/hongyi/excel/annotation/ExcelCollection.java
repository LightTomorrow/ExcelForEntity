package dh.hongyi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

/**
 * 标记字段为集合对象
 * @author HelloLight
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelCollection {

    /**
     * 标记集合唯一标识
     */
    String id() default "";

    /**
     * 标记集合名称
     */
    String name() default "";

    /**
     * 字段排序
     */
    String orderNum() default "0";

    /**
     * 创建时创建的类型 默认值是 ArrayList
     */
    Class<?> type() default ArrayList.class;
}

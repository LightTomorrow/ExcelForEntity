package dh.hongyi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识某个字段为唯一字段,因为在单头单身导入的情况下,可能存在多个相同的单头对应多条不同的单身明细,</br>
 * 使用这个注解可以标记出用哪个字段作为唯一键标识。</br>
 * 单头单身导入的情况下必须使用此注解,否则导入的数据会冗,如下例子:</br>
 * class SaleOrder{ </br>
 * 	 @ExcelUnique </br>
 * 	 private String formNo; //订单单号  </br>
 * 	 private Date orderDate; </br>
 * }  </br>
 * class SoLine { </br>
 * 	private String prodCode; </br>
 * 	private String prodName; </br>
 * }
 * 导入格式如下:
 * 订单单号	订单日期	 产品编码	产品名称 </br>
 * 100001	08-08	YX001	产品1 </br>
 * 100001	08-08	YX002	产品2 </br>
 * 100001	08-08	YX003	产品3 </br>
 * 以上情况,就可以使用到 {@link ExcelUnique}注解将订单号作为唯一标识字段, </br>
 * 那么在导入时就可以区分出是1个相同单号对应3笔产品明细。
 * @author HelloLight
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelUnique {

}

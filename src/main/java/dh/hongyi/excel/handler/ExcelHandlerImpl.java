package dh.hongyi.excel.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import dh.hongyi.excel.entity.Product;
import dh.hongyi.excel.entity.SaleOrder;
import dh.hongyi.excel.util.R;

/**
 * Excel解析映射到实体后的自定义业务逻辑处理
 * @author HelloLight
 */
@Component
public class ExcelHandlerImpl implements ExcelHandler{
	/**
	 * 订单数据导入处理
	 * @param saleOrders
	 * @return
	 */
	public R handlerSaleOrderDataImport(List<SaleOrder> saleOrders) {
		for (SaleOrder saleOrder : saleOrders) {
			System.err.println(saleOrder);
		}
		return R.ok();
	}
	
	/**
	 * 产品数据导入处理
	 * @param products
	 * @return
	 */
	public R productDataImport(List<Product> products) {
		for (Product product : products) {
			System.err.println(product);
		}
		return R.ok();
	}
}

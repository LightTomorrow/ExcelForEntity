package dh.hongyi.excel;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dh.hongyi.excel.config.ExcelConfig;
import dh.hongyi.excel.entity.Product;
import dh.hongyi.excel.entity.SaleOrder;
import dh.hongyi.excel.parse.ExcelDataParse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ExcelConfig.class)
public class ExcelForEntityTest {
	
	@Autowired
	private ExcelDataParse saleOrderExcelParse;
	
	@Autowired
	private ExcelDataParse productExcelParse;
	 
	/**
	 * 测试订单数据文件导入
	 * @throws Exception
	 */
	@Test
	public void testSaleOrderExcelDataImport() throws Exception {
		List<SaleOrder> saleOrderExcelDatas = saleOrderExcelParse.parse(SaleOrder.class);
		saleOrderExcelParse.executeExcelHandler(saleOrderExcelDatas);
	}
	
	/**
	 * 测试产品数据文件导入
	 * @throws Exception
	 */
	@Test
	public void testProductExcelDataImport() throws Exception {
		List<Product> products = productExcelParse.parse(Product.class);
		productExcelParse.executeExcelHandler(products);
	}
}

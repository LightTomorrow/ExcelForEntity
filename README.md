# ExcelForEntity
Excel数据解析映射到实体对象

[ExcelConfig](https://github.com/LightTomorrow/ExcelForEntity/blob/master/src/main/java/dh/hongyi/excel/config/ExcelConfig.java)配置类中实现解析对象的实例化。

```
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
```

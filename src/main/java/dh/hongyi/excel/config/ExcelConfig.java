package dh.hongyi.excel.config;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import dh.hongyi.excel.handler.ExcelHandler;
import dh.hongyi.excel.handler.ExcelImportConfig;
import dh.hongyi.excel.handler.TargetExcelFile;
import dh.hongyi.excel.parse.ExcelDataParse;
import dh.hongyi.excel.parse.ExcelDataParseImpl;
import dh.hongyi.excel.util.ExcelUtil;

@Configuration
@ComponentScan(basePackages="dh.hongyi")
public class ExcelConfig {

	@Autowired
	private ExcelHandler excelHandler;
	
	@Bean("saleOrderExcelParse")
	public ExcelDataParse getSaleOrderExcelParse() throws IOException {
		return new ExcelDataParseImpl(getSaleOrderExcelImportConfig(), excelHandler);
	}
	
	@Bean("productExcelParse")
	public ExcelDataParse getProductExcelParse() throws IOException {
		return new ExcelDataParseImpl(getProductExcelImportConfig(), excelHandler);
	}
	
	/**
	 * 订单数据导入配置,文件路径可以自己调整
	 * @return
	 * @throws IOException
	 */
	@Bean("saleOrderExcelImport")
	public ExcelImportConfig getSaleOrderExcelImportConfig() throws IOException {
		File excelFile = new ClassPathResource("/excel/订单数据.xlsx").getFile();
		ExcelImportConfig excelImportConfig = ExcelImportConfig.builder()
		.setExcelFile(new TargetExcelFile(excelFile))
		.setClassPath("dh.hongyi.excel.entity.SaleOrder")
		.setIncludeSingleImport(ExcelUtil.INCLUDE_SINGLE_IMPORT)
		.setValidationIfNeed(ExcelUtil.INCLUDE_SINGLE_IMPORT)
		.setHandlerMethod("handlerSaleOrderDataImport");
		return excelImportConfig;
	}
	
	@Bean("productExcelImport")
	public ExcelImportConfig getProductExcelImportConfig() throws IOException {
		File excelFile = new ClassPathResource("/excel/产品数据.xlsx").getFile();
		ExcelImportConfig excelImportConfig = ExcelImportConfig.builder()
		.setExcelFile(new TargetExcelFile(excelFile))
		.setClassPath("dh.hongyi.excel.entity.Product")
		.setHandlerMethod("productDataImport");
		return excelImportConfig;
	} 
}

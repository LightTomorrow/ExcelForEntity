package dh.hongyi.excel.entity;

import java.math.BigDecimal;

import dh.hongyi.excel.annotation.ExcelImport;
import lombok.Data;

@Data
public class SoLine {
	
	@ExcelImport(name="产品编码",require=true)
	private String prodCode;
	
	@ExcelImport(name="产品编码",require=true)
	private String prodName;
	
	@ExcelImport(name="产品规格")
	private String prodScale;
	
	@ExcelImport(name="销售价",require=true)
	private BigDecimal price;
	
	@ExcelImport(name="数量",require=true)
	private BigDecimal qty;
	
	@ExcelImport(name="行小计金额",require=true)
	private BigDecimal lineAmount;
 
}
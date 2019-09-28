package dh.hongyi.excel.entity;

import java.util.Date;
import java.util.List;

import dh.hongyi.excel.annotation.ExcelCollection;
import dh.hongyi.excel.annotation.ExcelImport;
import dh.hongyi.excel.annotation.ExcelUnique;
import lombok.Data;

@Data
public class SaleOrder {
	
	@ExcelUnique
	@ExcelImport(name="订单编号",require=true)
	private String formNo;
	
	@ExcelImport(name="订单日期",require=true)
	private Date formDate;
	
	@ExcelImport(name="经销商名称",require=true)
	private String dealerName;
	
	@ExcelImport(name="经销商编码",require=true)
	private String dealerCode;
	
	@ExcelImport(name="经销商简称")
	private String dealerAbbr;
	
	@ExcelCollection
	private List<SoLine> soLines;
}
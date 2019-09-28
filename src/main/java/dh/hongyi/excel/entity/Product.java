package dh.hongyi.excel.entity;

import dh.hongyi.excel.annotation.ExcelImport;
import dh.hongyi.excel.annotation.ExcelUnique;

public class Product {
	
	@ExcelUnique
	@ExcelImport(name="产品编码")
	private String prodCode;
	
	@ExcelImport(name="产品名称")
	private String prodName;

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	@Override
	public String toString() {
		return "Product [prodCode=" + prodCode + ", prodName=" + prodName + "]";
	}
}

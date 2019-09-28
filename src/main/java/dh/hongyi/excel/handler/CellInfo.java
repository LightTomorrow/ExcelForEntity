package dh.hongyi.excel.handler;

/**
 * 封装Excel单元格数据
 * @author HelloLight
 */
public class CellInfo {
	/**
	 * 单元格行号
	 */
	private int rowNum;
	/**
	 * 单元格列号
	 */
	private int columnNum;
	/**
	 * 单元格的值
	 */
	private Object cellVal;
	/**
	 * 单元格对应的类型字节对象
	 */
	private Class<?> clazz;

	public int getRowNum() {
		return rowNum;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public Object getCellVal() {
		return cellVal;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	public void setCellVal(Object cellVal) {
		this.cellVal = cellVal;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
}

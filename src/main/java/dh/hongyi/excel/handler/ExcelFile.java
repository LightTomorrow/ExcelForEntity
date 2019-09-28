package dh.hongyi.excel.handler;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel文件
 * @author HelloLight
 *
 */
public interface ExcelFile {

	/**
	 * 创建ExcelWorkbook对象
	 * @return
	 * @throws Exception
	 */
	Workbook createWorkBook() throws Exception;
}

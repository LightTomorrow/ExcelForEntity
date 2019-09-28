package dh.hongyi.excel.handler;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import dh.hongyi.excel.util.ExcelUtil;

/**
 * 适用于指定Excel文件的绝对路径来创建Workbook对象
 * @author HelloLight
 */
public class TargetExcelFile implements ExcelFile{

	private String filePath;
	
	private File file;
	
	public TargetExcelFile(String filePath) {
		Assert.notNull(filePath, "文件绝对路径不能为空");
		this.filePath = filePath;
	}
	
	public TargetExcelFile(File file) {
		Assert.notNull(file, "文件不能为空");
		this.file = file;
	}

	@Override
	public Workbook createWorkBook() throws Exception {
		if (file != null) {
			return ExcelUtil.createWorkBook(new FileInputStream(file));
		} 
		if (StringUtils.hasLength(filePath)) {
			return ExcelUtil.createWorkBook(filePath);
		}
		throw new IllegalArgumentException("文件或文件绝对路径不能为空");
	}

}

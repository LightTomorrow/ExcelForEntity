package dh.hongyi.excel.handler;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import dh.hongyi.excel.util.ExcelUtil;

/**
 * 适用于前台传入的Excel文件,根据传入的文件来创建Workbook对象
 * @author HelloLight
 *
 */
public class MultipartExcelFile implements ExcelFile {

	private MultipartFile multipartFile;
	
	public MultipartExcelFile(MultipartFile multipartFile) {
		Assert.notNull(multipartFile, "文件不能为空");
		this.multipartFile = multipartFile;
	}

	@Override
	public Workbook createWorkBook() throws Exception {
		return ExcelUtil.createWorkBook(multipartFile.getInputStream());
	}

}

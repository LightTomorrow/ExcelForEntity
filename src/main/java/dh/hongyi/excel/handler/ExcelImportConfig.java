package dh.hongyi.excel.handler;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.Assert;

import dh.hongyi.excel.util.ExcelUtil;

/**
 * Excel导入配置
 * @author HelloLight
 */
public class ExcelImportConfig {
	/**
	 * Excel文件类型
	 */
	private ExcelFile excelFile;
	/**
	 * 需要映射的实体全路径
	 */
	private String classPath;
	
	/**
	 * 数据解析方式 1-自动解析,2-手动解析
	 */
	private Integer parseWay = ExcelUtil.AUTO_MAPPER_PARSE;
	 
	/**
	 * 是否包含单身导入-Y:是,N:否
	 */
	private String includeSingleImport;
	/**
	 * 是否需要校验校验字段为必填
	 */
	private String validationIfNeed;
	/**
	 * Excel解析映射到实体后的处理方法,</br>
	 * 该方法在{@link dh.hongyi.excel.handler.ExcelHandlerImpl}中实现
	 */
	private String handlerMethod;
	
	private ExcelImportConfig() {
		
	}
	
	/**
	 * 构造Excel导入配置对象,</br>
	 * 是否包含单身导入与是否需要校验校验字段为必填默认为是
	 * @param excelFile Excel文件
	 * @param classPath 需要解析映射类的全路径
	 * @param parseWay 解析方式
	 * @param handlerMethod Excel解析映射到实体后的处理方法
	 */
	public ExcelImportConfig(ExcelFile excelFile, String classPath, Integer parseWay,String handlerMethod) {
		this(excelFile,classPath,parseWay,"Y","Y",handlerMethod);
	}
	
	/**
	 * 构造Excel导入配置对象
	 * @param excelFile Excel文件
	 * @param classPath 需要解析映射类的全路径
	 * @param parseWay 解析方式
	 * @param includeSingleImport 是否包含单身导入-Y:是,N:否
	 * @param validationIfNeed 是否需要校验校验字段为必填
	 * @param handlerMethod Excel解析映射到实体后的处理方法
	 */
	public ExcelImportConfig(ExcelFile excelFile, String classPath, Integer parseWay,String includeSingleImport, String validationIfNeed, String handlerMethod) {
		Assert.notNull(excelFile, "Excel文件对象不能为空!");
		Assert.notNull(classPath, "映射类的全路径不能为空!");
		Assert.notNull(parseWay, "解析方式不能为空!");
		Assert.notNull(includeSingleImport, "是否包含单身导入不能为空!");
		Assert.notNull(validationIfNeed, "是否需要校验校验字段不能为空!");
		Assert.notNull(validationIfNeed, "Excel映射实体后的处理方法不能为空!");
		this.excelFile = excelFile;
		this.classPath = classPath;
		this.parseWay = parseWay;
		this.includeSingleImport = includeSingleImport;
		this.validationIfNeed = validationIfNeed;
		this.handlerMethod = handlerMethod;
	}
	
	/**
	 * 调用此方法,请给所有set方法赋值
	 * @return
	 */
	public static ExcelImportConfig builder() {
		return new ExcelImportConfig();
	}

	/**
	 * 是否需要校验字段为必填
	 * @return true:需要校验,false:不需要校验
	 */
	public boolean isValidation(){
		return "Y".equalsIgnoreCase(getValidationIfNeed()) ? true : false;
	}
	/**
	 * 创建Workbook对象
	 * @return
	 * @throws Exception
	 */
	public Workbook getWorkBook() throws Exception {
		return excelFile.createWorkBook();
	}
	
	public ExcelFile getExcelFile() {
		return excelFile;
	}

	public ExcelImportConfig setExcelFile(ExcelFile excelFile) {
		this.excelFile = excelFile;
		return this;
	}

	public String getClassPath() {
		return classPath;
	}
	public ExcelImportConfig setClassPath(String classPath) {
		this.classPath = classPath;
		return this;
	}
	public Integer getParseWay() {
		return parseWay;
	}
	public ExcelImportConfig setParseWay(Integer parseWay) {
		this.parseWay = parseWay;
		return this;
	}
	public String getIncludeSingleImport() {
		return includeSingleImport;
	}
	public ExcelImportConfig setIncludeSingleImport(String includeSingleImport) {
		this.includeSingleImport = includeSingleImport;
		return this;
	}
	public String getValidationIfNeed() {
		return validationIfNeed;
	}
	public ExcelImportConfig setValidationIfNeed(String validationIfNeed) {
		this.validationIfNeed = validationIfNeed;
		return this;
	}
	public String getHandlerMethod() {
		return handlerMethod;
	}
	public ExcelImportConfig setHandlerMethod(String handlerMethod) {
		this.handlerMethod = handlerMethod;
		return this;
	}
}
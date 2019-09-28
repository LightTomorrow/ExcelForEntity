package dh.hongyi.excel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import dh.hongyi.excel.annotation.ExcelCollection;
import dh.hongyi.excel.annotation.ExcelImport;
import dh.hongyi.excel.annotation.ExcelUnique;
import dh.hongyi.excel.exception.GenericBusinessException;
import dh.hongyi.excel.handler.CellInfo;
import dh.hongyi.excel.handler.RowInfo;
/**
 * Excel工具类 
 * @author HelloLight
 */
public class ExcelUtil {
	
	/**
	 * 自动解析Excel内容到指定对象中
	 */
	public static final int AUTO_MAPPER_PARSE = 1;
	/**
	 * 自定义解析Excel内容到指定对象中
	 */
	public static final int CUSTOM_MAPPER_PARSE = 2;
	/**
	 * 包含单身导入
	 */
	public static final String INCLUDE_SINGLE_IMPORT = "Y";
	
	/**
	 * 根据文件输入流创建Excel对象
	 * @param inputStream
	 * @return 如果Excel文件是2003之前的版本则返回的对象HSSFWorkbook,否则返回的是XSSFWorkbook
	 * @throws Exception
	 */
	public static Workbook createWorkBook(InputStream inputStream) throws Exception{
		Assert.notNull(inputStream, "文件输入流不能为空");
		return WorkbookFactory.create(inputStream);
	}
	/**
	 * 根据文件全路径创建Excel对象
	 * @param inputStream
	 * @return 如果Excel文件是2003之前的版本则返回的对象HSSFWorkbook,否则返回的是XSSFWorkbook
	 * @throws Exception
	 */
	public static Workbook createWorkBook(String fileFullPath) throws Exception{
		Assert.notNull(fileFullPath, "文件路径不能为空");
		return WorkbookFactory.create(new FileInputStream(new File(fileFullPath)));
	}
	
	/**
	 * 解析Excel中的行数据
	 * @param workbook
	 * @return Excel行数据,第0个元素为标题数据,第1个及之后的元素为业务数据
	 */
	public static List<RowInfo> getWorkbookRowInfos(Workbook workbook) {
		Sheet xssfSheet = workbook.getSheetAt(0);
		if(xssfSheet == null || xssfSheet.getRow(0) == null) { 
			return Collections.emptyList(); 
		}
		//处理Excel标题(默认为第1行)
		int rowNum = xssfSheet.getLastRowNum();
		List<RowInfo> rowInfos = new ArrayList<RowInfo>(rowNum);
		Row titleRow = xssfSheet.getRow(0);
		handleExcelRowData(rowInfos,titleRow,0);
		//处理Excel数据(默认起始于第2行)
		int loopCount = rowInfos.get(0).getCellInfos().size(); //标题
		DataFormatter dataFormatter = new DataFormatter();
		for (int i = 1; i <= rowNum; i++) {
			Row dataRow = xssfSheet.getRow(i); //每一行的数据
			if (!checkRowIsEmpty(dataFormatter,dataRow)) {
				handleRowDataByExcelTitle(rowInfos, dataRow, rowNum, loopCount);
			}
		}
		return rowInfos;
	}
	
	/**
	 * 处理Excel单元格数据
	 * @param rowInfos  封装每一个行上的单元格数据
	 * @param row Excel表格中具体行项目
	 * @param rowNum 记录行数
	 */
	public static void handleExcelRowData(List<RowInfo> rowInfos, Row row,int rowNum) {
		List<CellInfo> cellInfos = new ArrayList<CellInfo>(row.getPhysicalNumberOfCells());
		RowInfo rowInfo = new RowInfo();
		for (int j = 0,lastCellNum = row.getLastCellNum(); j < lastCellNum; j++) {
		    cellInfos.add(doHandlerCell(row.getCell(j),row,j,rowNum));
		}
		rowInfo.setCellInfos(cellInfos);
		rowInfos.add(rowInfo);
	}
	
	/**
	 * 处理Excel单元格数据
	 * @param rowInfos  封装每一个行上的单元格数据
	 * @param row Excel表格中具体行项目
	 * @param rowNum 记录行数
	 */
	public static void handleRowDataByExcelTitle(List<RowInfo> rowInfos, Row row,int rowNum,int loopCount) {
		List<CellInfo> cellInfos = new ArrayList<CellInfo>(loopCount);
		for (int j = 0;j < loopCount; j++) {
		    cellInfos.add(doHandlerCell(row.getCell(j),row,j,rowNum));
		}
		RowInfo rowInfo = new RowInfo();
		rowInfo.setCellInfos(cellInfos);
		rowInfos.add(rowInfo);
	}
	
	/**
	 * 处理Excel单元格数据
	 * @param cell Excel单元格
	 * @param row 行数据对象
	 * @param columnNum  单元格所处列数
	 * @param rowNum 单元格所处行数
	 * @return  单元格数据
	 */
	private static CellInfo doHandlerCell(Cell cell,Row row,int columnNum,int rowNum){
		CellInfo cellInfo = new CellInfo();
		if(cell == null) {
			cellInfo.setCellVal("");
			cellInfo.setClazz(String.class);
	        cellInfo.setRowNum(rowNum+1);
	        cellInfo.setColumnNum(columnNum+1);
	        return cellInfo;
		}
		Object cellVal = null;
	    switch (cell.getCellTypeEnum()) {
	        case STRING:
	        	cellVal = cell.getRichStringCellValue().getString().trim();
	        	cellInfo.setClazz(String.class);
	            break;
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	            	cellVal = cell.getDateCellValue();
	            	cellInfo.setClazz(Date.class);
	            } else {
	                cellVal = doubleToBigDecimal(cell.getNumericCellValue());
	            	cellInfo.setClazz(BigDecimal.class);
	            }
	            break;
	        case BOOLEAN:
	        	cellVal = cell.getBooleanCellValue();
	        	cellInfo.setClazz(Boolean.class);
	            break;
	        case FORMULA:
	        	cellVal = cell.getCellFormula();
	        	cellInfo.setClazz(String.class);
	            break;
	        case BLANK:
	        default:
	        	cellVal = "";
	        	cellInfo.setClazz(String.class);
	    }
	    cellInfo.setCellVal(cellVal);
	    cellInfo.setRowNum(row.getRowNum());
	    cellInfo.setColumnNum(cell.getColumnIndex()+1);
	    return cellInfo;
	}
	
	/**
	 * 将double转成BigDecimal类型
	 * @param doubleVal
	 * @return
	 */
	public static BigDecimal doubleToBigDecimal(double doubleVal){
		 BigDecimal bigDecimal = new BigDecimal(Double.toString(doubleVal));
		 //去掉后面无用的零  如小数点后面全是零则去掉小数点
		 return new BigDecimal(bigDecimal.toPlainString().replaceAll("0+?$", "").replaceAll("[.]$", ""));
	}
	
	/**
	 * 校验Excel行数据是否为空
	 * @param row
	 * @return true:为空,false:不为空
	 */
	public static boolean checkRowIsEmpty(DataFormatter dataFormatter,Row row){
		if(row == null) { return true; }
        for(Cell cell: row) {
            if(checkCellIsNotEmpty(dataFormatter,cell)) {
                return false;
            }
        }
		 return true;
	}
	
	/**
	 * 校验Excel中每个单元格是否为空
	 * @param dataFormatter
	 * @param cell
	 * @return true:不为空,false:为空
	 */
	private static boolean checkCellIsNotEmpty(DataFormatter dataFormatter,Cell cell){
		if(cell == null) { return false; }
		return dataFormatter.formatCellValue(cell).trim().length() > 0;
	}
	
	/**
	 * 获取Excel标题
	 * @param rowInfos
	 * @return Excel标题 k-标题,v-标题所在列号
	 */
	public static Map<String, Integer> getExcelTitles(List<RowInfo> rowInfos){
		List<CellInfo> titleCellInfos = rowInfos.get(0).getCellInfos();
		Assert.notNull(titleCellInfos, "Excel标题不能为空");
		
		Map<String, Integer> titleMap = new LinkedHashMap<String, Integer>(titleCellInfos.size());
		for (CellInfo cellInfo : titleCellInfos) {
			String cellVal = String.valueOf(cellInfo.getCellVal());
			if(titleMap.containsKey(cellInfo.getCellVal())){
				throw new IllegalArgumentException(MessageFormat.format("Excel文件{0}列名重复定义", cellInfo.getCellVal()));
			}
			titleMap.put(cellVal, cellInfo.getColumnNum());
		}
		return titleMap;
	}
	
	/**
	 * 根据指定唯一标识字段将Excel多行数组进行分组,这样便可以知道此单头对应几笔单身明细
	 * @param clazz
	 * @param rowInfos
	 * @param titleMap
	 * @return
	 * @throws GenericBusinessException
	 */
	public static Map<String, List<RowInfo>> groupForRowInfos(Class<?> clazz,List<RowInfo> rowInfos, Map<String, Integer> titleMap) throws GenericBusinessException {
		Field excelUniqueField = ExcelUtil.getExcelUniqueField(clazz);
		ExcelImport excelImport = excelUniqueField.getAnnotation(ExcelImport.class);
		if (!titleMap.containsKey(excelImport.name())) {
			throw new GenericBusinessException("Excel导入标题与字段声明标题匹配失败,请检查Excel导入标题!");
		}
		Integer columnNo = titleMap.get(excelImport.name());
		Map<String, List<RowInfo>> groupRowInfoMap = new LinkedHashMap<String, List<RowInfo>>();
		for (int i = 1; i < rowInfos.size(); i++) {
			RowInfo rowInfo = rowInfos.get(i);
			List<CellInfo> cellInfos = rowInfo.getCellInfos();
			CellInfo cellInfo = cellInfos.get(columnNo-1);
			String key = String.valueOf(cellInfo.getCellVal());
			Assert.hasText(key, MessageFormat.format("第{0}行的唯一标识字段[{1}]的值不能为空!", (cellInfo.getRowNum()),excelImport.name()));
			
			if (groupRowInfoMap.containsKey(key)) {
				List<RowInfo> list = groupRowInfoMap.get(key);
				list.add(rowInfo);
				groupRowInfoMap.put(key, list);
			} else {
				List<RowInfo> list = new ArrayList<RowInfo>();
				list.add(rowInfo);
				groupRowInfoMap.put(key, list);
			}
		}
		return groupRowInfoMap;
	}
	
	/**
	 * 处理Excel单头数据
	 * @param clazz
	 * @param rowInfos
	 * @param titleMap
	 * @return
	 * @throws Exception
	 */
	public static List<Object> handlerExcelData(Class<?> clazz,List<RowInfo> rowInfos, Map<String, Integer> titleMap) throws Exception{
		List<Object> dataObject = new ArrayList<Object>(rowInfos.size() / 2);
		Map<Object, Object> uniqueObjMap = new LinkedHashMap<Object, Object>(rowInfos.size() / 2);
		int titleColumnNo = ExcelUtil.getUniqueFieldTitleColumnNo(clazz, titleMap);
		
		for (int i = 1; i < rowInfos.size(); i++) {
			List<CellInfo> cellInfos = rowInfos.get(i).getCellInfos();
			if(cellInfos.size() != titleMap.size()) {
				throw new GenericBusinessException(MessageFormat.format("列数大小[{0}]与标题个数[{1}]不一致!", cellInfos.size(),titleMap.size()));
			}
			Object uniqueCellVal = cellInfos.get(titleColumnNo).getCellVal();
			if (!uniqueObjMap.containsKey(uniqueCellVal)) {
				Object obj = doGetObjectData(clazz, cellInfos, titleMap);
				if (obj != null) {
					dataObject.add(obj);
					uniqueObjMap.put(uniqueCellVal, obj);
				}
			} 
		}
		return dataObject;
	}
	
	/**
	 * 处理Excel单身数据
	 * @param clazz
	 * @param objectData
	 * @param titleMap
	 * @param groupMap
	 * @throws Exception
	 */
	public static void handlerExcelDatas(Class<?> clazz, List<Object> objectData,Map<String, Integer> titleMap, Map<String, List<RowInfo>> groupMap) throws Exception {
		List<Field> excelCollectionFields = ExcelUtil.getExcelCollectionFields(clazz);
		if (!CollectionUtils.isEmpty(objectData) && !excelCollectionFields.isEmpty()) {
				for (Object object : objectData) {
					List<RowInfo> rowInfos = ExcelUtil.getExcelCollectionByObjectUniqueValue(object, groupMap);
					if (!CollectionUtils.isEmpty(rowInfos)) {
						for (Field collectionField : excelCollectionFields) {
							Class<?> actualClass = ClassUtil.getActualClassForField(collectionField);
							List<Object> lineObjects = new ArrayList<Object>(rowInfos.size());
							for (RowInfo rowInfo : rowInfos) {
								Object lineObject = doGetObjectData(actualClass, rowInfo.getCellInfos(), titleMap);
								if (lineObject != null) {
									lineObjects.add(lineObject);
								}
							}
							ClassUtil.setAccessible(collectionField);
							collectionField.set(object, lineObjects);
						}
					}
				}
		}
	}
	
	/**
	 * 封装指定对象的值
	 * @param clazz
	 * @param cellInfos
	 * @param titleMap
	 * @return
	 * @throws Exception
	 */
	private static Object doGetObjectData(Class<?> clazz,List<CellInfo> cellInfos, Map<String, Integer> titleMap) throws Exception{
		List<Field> excelFields = ExcelUtil.getExcelImportFields(clazz);
		if (!excelFields.isEmpty()) {
			Object obj = ClassUtil.createInstance(clazz);
			for (Field field : excelFields) {
				ExcelImport excelImport = field.getAnnotation(ExcelImport.class);
				if (titleMap.containsKey(excelImport.name())) {
					Integer column = titleMap.get(excelImport.name());
					CellInfo cellInfo = cellInfos.get(column-1);
					setFieldVal(obj, field,cellInfo,excelImport,cellInfo.getRowNum());
				}
			}
			return obj;
		}
		return null;
	}
	
	/**
	 * 设置字段的值
	 * @param targetObj 字段所属对象,用于设值
	 * @param field 指定给哪个字段赋值
	 * @param fieldVal 字段值
	 * @param fieldValType 字段类型
	 * @param excelImport 标记字段为Excel导入字段
	 * @throws Exception
	 */
	private static void setFieldVal(Object targetObj,Field field,CellInfo cellInfo, ExcelImport excelImport,int rowNum) throws Exception{
		Object fieldVal = cellInfo.getCellVal();
		if (fieldVal != null && !"".equals(fieldVal)) {
			ClassUtil.setAccessible(field);
			String fieldStrVal = String.valueOf(fieldVal);
			Class<?> fieldValType = cellInfo.getClazz();
			if (Date.class.isAssignableFrom(field.getType())) {
				if (Date.class.isAssignableFrom(fieldValType)) {
					field.set(targetObj, (Date) fieldVal);
				} else {
					field.set(targetObj, DateUtils.parseStrToDate(excelImport.dateFormat(), fieldStrVal));
				} 
			} else if (String.class.isAssignableFrom(field.getType())) {
				 if (Number.class.isAssignableFrom(fieldValType)) {
					Number number = (Number) fieldVal;
					field.set(targetObj, String.valueOf(number.toString()));
				 }  else {
					 field.set(targetObj, fieldStrVal);
				 }
			} else if (Number.class.isAssignableFrom(field.getType())) {
				field.set(targetObj, Utils.strTransToNumber(field, fieldStrVal, fieldValType));
			} else {
				field.set(targetObj, fieldVal);
			}
		}
		if (is_validator_data.get() && excelImport.require()) {
			 validatorFieldVal(field, targetObj, excelImport, rowNum);
		}
	}
	
	
	/**
	 * 校验字段值
	 * @param field
	 * @param targetObj
	 * @param excelImport
	 * @param rowNum
	 * @throws GenericBusinessException
	 */
	private static void validatorFieldVal(Field field,Object targetObj,ExcelImport excelImport,int rowNum) throws GenericBusinessException{
		Object objectVal = null;
		try {
			ClassUtil.setAccessible(field);
			objectVal = field.get(targetObj);
		} catch (Exception e) {
			throw new GenericBusinessException(e.getMessage());
		}
		if (objectVal == null || (objectVal instanceof String && StringUtils.isEmpty((String) objectVal)) ) {
			throw new GenericBusinessException(MessageFormat.format("第{0}行的{1}不能为空!", (rowNum+1), excelImport.name()));
		}
	}
	
	/**
	 * 从分好组的数据中根据对象唯一值获取单身明细
	 * @param obj
	 * @param groupMap
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static List<RowInfo> getExcelCollectionByObjectUniqueValue(Object obj,Map<String, List<RowInfo>> groupMap) throws Exception{
		Field field = getExcelUniqueField(obj.getClass());
		field.setAccessible(true);
		Object uniqueKey = field.get(obj);
		return groupMap.get(uniqueKey);
	}
	
	/**
	 * 获取指定对象中标记唯一标识的字段
	 * @param clazz
	 * @return
	 * @throws GenericBusinessException 
	 */
	public static Field getExcelUniqueField(Class<?> clazz) throws GenericBusinessException{
		Field[] classFields = ClassUtil.getClassFields(clazz);
		for (Field field : classFields) {
			if (field.isAnnotationPresent(ExcelImport.class) && field.isAnnotationPresent(ExcelUnique.class)) {
				if (!String.class.isAssignableFrom(field.getType())) {
					throw new GenericBusinessException(MessageFormat.format("{0}类中的字段{1}类型必须为String", clazz.getName(), field.getName()));
				}
				return field;
			}
		}
		throw new GenericBusinessException(MessageFormat.format("{0}类未使用@Excel,@ExcelUnique注解标识唯一字段", clazz.getName()));
	}
	
	/***
	 * 获取需要导入的Excel字段,</br>
	 * 字符如果使用了 @{link dh.hongyi.platform.annotation.ExcelImport } 注解表示该字段是需要导入处理的字段
	 * @param clazz 
	 * @return
	 */
	public static List<Field> getExcelImportFields(Class<?> clazz){
		Field[] classFields = ClassUtil.getClassFields(clazz);
		List<Field> excelFields = new ArrayList<Field>(classFields.length);
		for (Field field : classFields) {
			if(!ClassUtil.isPublicStaticFinal(field) && ClassUtil.hasAnnotation(field,ExcelImport.class)) {
				if(!ClassUtil.isCollection(field) && !ClassUtil.isMap(field)){
					excelFields.add(field);
				}
			}
		}
		return excelFields;
	}
	/**
	 * 获取指定对象下的字段为@{link java.util.Collection}类型,</br>
	 * 并使用了@{link dh.hongyi.platform.annotation.ExcelCollection}注解的字段
	 * @param clazz
	 * @return
	 */
	public static List<Field> getExcelCollectionFields(Class<?> clazz){
		Field[] classFields = ClassUtil.getClassFields(clazz);
		List<Field> excelCollectionFields = new ArrayList<Field>(classFields.length);
		for (Field field : classFields) {
			if (!ClassUtil.isPublicStaticFinal(field) && ClassUtil.hasAnnotation(field,ExcelCollection.class)) {
				if(ClassUtil.isCollection(field)) {
					excelCollectionFields.add(field);
				}
			}
		}
		return excelCollectionFields;
	}
	
	/**
	 * 获取指定对象中的唯一标识字段名称在Excel文件标题中的列号
	 * @param clazz
	 * @param titleMap
	 * @return
	 * @throws GenericBusinessException
	 */
	public static int getUniqueFieldTitleColumnNo(Class<?> clazz, Map<String, Integer> titleMap) throws GenericBusinessException {
		Field excelUniqueField = getExcelUniqueField(clazz);
		ExcelImport excelImport = excelUniqueField.getAnnotation(ExcelImport.class);
		if (!titleMap.containsKey(excelImport.name())) { 
			throw new GenericBusinessException("Excel文件标题不存在与唯一标识字段"+excelUniqueField.getName()+"声明的标题"+excelImport.name());
		}
		return (titleMap.get(excelImport.name()).intValue()) - 1; //减1的目的是list索引从0开始
	}
	
	 /**
	  * 存放是否需要校验字段必填项
     */
    public static ThreadLocal<Boolean> is_validator_data = new ThreadLocal<Boolean>(){
        @Override 
        protected Boolean initialValue(){
            return false;
        }
    };
}
package dh.hongyi.excel.parse;

import java.util.List;

import dh.hongyi.excel.handler.ExcelHandler;
import dh.hongyi.excel.handler.ExcelImportConfig;

/**
 * 
 * @author HelloLight
 */
public interface ExcelDataParse {
	/**
	 * Excel数据解析处理方法-主要逻辑在此方法中实现
	 * @return 处理完成的数据
	 * @throws Exception
	 */
	List<Object> parse() throws Exception;
	
	/**
	 * 根据传入的class类型转换成指定的List类型
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	<T> List<T> parse(Class<T> clazz) throws Exception;
	
	/**
	 * 获取Excel导入配置</br>
	 * {@link dh.hongyi.excel.handler.ExcelImportConfig}
	 * @return
	 */
	ExcelImportConfig getExcelImportConfig();
	
	/**
	 * 封装Excel导入配置</br>
	 * {@link dh.hongyi.excel.handler.ExcelImportConfig}
	 * @param excelImportConfig
	 */
	
	void setExcelImportConfig(ExcelImportConfig excelImportConfig);
	
	/**
	 * Excel数据处理接口{@link dh.hongyi.excel.handler.ExcelHandler},</br>
	 * 由子类{@link dh.hongyi.excel.handler.ExcelHandlerImpl}负责实现业务处理方法
	 * @return
	 */
	ExcelHandler getExcelHandler();
	
	/**
	 * 封装Excel数据处理接口
	 * @param excelHandler
	 */
	void setExcelHandler(ExcelHandler excelHandler);
	
	/**
	 * 调用业务逻辑方法-这里是解析完成后,需要对数据做自定义加功处理,比如保存操作。</br>
	 * 所以的自定义处理方法在{@link dh.hongyi.excel.handler.ExcelHandlerImpl}中实现
	 * @param dataList
	 * @return
	 * @throws Exception
	 */
	Object executeExcelHandler(List<?> dataList) throws Exception;
}

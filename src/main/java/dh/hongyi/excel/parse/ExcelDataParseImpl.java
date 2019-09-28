package dh.hongyi.excel.parse;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import dh.hongyi.excel.exception.GenericBusinessException;
import dh.hongyi.excel.handler.ExcelHandler;
import dh.hongyi.excel.handler.ExcelImportConfig;
import dh.hongyi.excel.handler.RowInfo;
import dh.hongyi.excel.util.ClassUtil;
import dh.hongyi.excel.util.ExcelUtil;
import lombok.Data;

/**
 * Excel数据解析实现类
 * 
 * @author HelloLight
 **/
@Data
public class ExcelDataParseImpl implements ExcelDataParse {
 
	private static final Logger logger = Logger.getLogger(ExcelDataParse.class.getName());
	/**
	 * initialization empty list
	 */
	public static final List<Object> EMPTY_LIST = Collections.emptyList();
	
	private ExcelImportConfig excelImportConfig;
	
	private ExcelHandler excelHandler;
	
	public ExcelDataParseImpl(ExcelImportConfig excelImportConfig,ExcelHandler excelHandler) {
		Assert.notNull(excelImportConfig, "Excel导入配置不能为空");
		Assert.notNull(excelHandler, "ExcelHandler类不能为空");
		this.excelImportConfig = excelImportConfig;
		this.excelHandler = excelHandler;
	}
	
	@Override
	public List<Object> parse() throws Exception{
		Workbook workbook = excelImportConfig.getWorkBook();
		if (ExcelUtil.AUTO_MAPPER_PARSE == excelImportConfig.getParseWay()) {
			return autoMapperParse(workbook,excelImportConfig);
		} 
		else if (ExcelUtil.CUSTOM_MAPPER_PARSE == excelImportConfig.getParseWay()) {
			return customMapperParse(workbook,excelImportConfig);
		} 
		else {
			logger.info("导入数据配置未指定解析方式!");
		}
		return EMPTY_LIST;
	}

	/**
	 * 自动映射解析
	 * @param workbook
	 * @param excelImportConfig
	 * @return
	 * @throws Exception
	 */
	private List<Object> autoMapperParse(Workbook workbook, ExcelImportConfig excelImportConfig) throws Exception {
		List<RowInfo> rowInfos = ExcelUtil.getWorkbookRowInfos(workbook);
		if (!rowInfos.isEmpty() && rowInfos.size() > 1) {
			//设置是否需要进行字段值为空校验
			ExcelUtil.is_validator_data.set(excelImportConfig.isValidation());
			//加载需要被映射的类
			Class<?> targetClass = ClassUtil.loadClass(excelImportConfig.getClassPath());
			//将Excel标题及标题所在列转换成Map对象 k-标题,v-列号
			Map<String, Integer> titleMap = ExcelUtil.getExcelTitles(rowInfos);
			//初始化一个空集合,用于接收数据
			List<Object> objectData = EMPTY_LIST;
			//是否包含单身导入:Y:包含,N:不包含
			if (isIncludeSingleImport(excelImportConfig)) {
				//拆分单身信息(单头单身情况导入)
				Map<String, List<RowInfo>> groupMap = ExcelUtil.groupForRowInfos(targetClass,rowInfos,titleMap);
				//处理单头数据
				objectData = ExcelUtil.handlerExcelData(targetClass, rowInfos,titleMap);
				//处理单身数据
				ExcelUtil.handlerExcelDatas(targetClass,objectData,titleMap,groupMap);
			} 
			else {
				//处理只有单头情况导入
				objectData = ExcelUtil.handlerExcelData(targetClass, rowInfos,titleMap);
			}
			return objectData;
		}
		return EMPTY_LIST;
	}

	/**
	 * 自定义映射解析
	 * @param workbook
	 * @param excelImportConfig
	 * @return
	 */
	private List<Object> customMapperParse(Workbook workbook, ExcelImportConfig excelImportConfig) {
		//需要自己实现
		return EMPTY_LIST;
	}
	
	@Override
	public Object executeExcelHandler(List<?> data) throws Exception{
		if (excelHandler == null) {
			throw new GenericBusinessException("dh.hongyi.excel.entity.ExcelHandler不能为空!");
		}
		if (StringUtils.isEmpty(excelImportConfig.getHandlerMethod())) {
			throw new GenericBusinessException("dh.hongyi.excel.parse.ExcelDataParse.excelImportConfig.getHandlerMethod()值(处理自己业务逻辑的方法)不能为空!");
		}
		Method method = null;
		try {
			method = excelHandler.getClass().getMethod(excelImportConfig.getHandlerMethod().trim(), List.class);
		} 
		catch(NoSuchMethodException exception) {
			throw new GenericBusinessException("dh.hongyi.excel.entity.ExcelHandler实现类未定义"+excelImportConfig.getHandlerMethod()+"方法!");
		}
		return method.invoke(excelHandler, data);
	}
 
	/***
	 * 判断是否包含单身导入
	 * Y:包含单身导入,N:不包含单身导入
	 * @param excelImportConfig
	 * @return
	 */
	private boolean isIncludeSingleImport(ExcelImportConfig excelImportConfig){
		return ExcelUtil.INCLUDE_SINGLE_IMPORT.equalsIgnoreCase(excelImportConfig.getIncludeSingleImport()) ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parse(Class<T> clazz) throws Exception {
		return (List<T>) parse();
	}
}

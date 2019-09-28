package dh.hongyi.excel.handler;

import java.util.List;

/**
 * 封装Excel行数据
 * @author HelloLight
 */
public class RowInfo {

	private List<CellInfo> cellInfos;
	
	public List<CellInfo> getCellInfos() {
		return cellInfos;
	}
	public void setCellInfos(List<CellInfo> cellInfos) {
		this.cellInfos = cellInfos;
	}
	
}

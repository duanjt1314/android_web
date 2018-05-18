package duanjt.life.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟实现DataTable相关功能
 * 
 * @author 段江涛
 *
 */
public class DataTable extends ArrayList<DataRow> {
	/**
	 * 当前对象是否为空或数据条数为0
	 * 
	 * @return
	 */
	public boolean isNullOrEmpty() {
		if (this == null || this.size() == 0)
			return true;
		return false;
	}

	/**
	 * 获得列的集合
	 * 
	 * @return
	 */
	public String[] getColumns() {
		if (!isNullOrEmpty()) {
			List<String> list = new ArrayList<String>();
			for (String s : this.get(0).keySet()) {
				list.add(s);
			}
			return list.toArray(new String[list.size()]);
		} else {
			return new String[] {};
		}
	}
}

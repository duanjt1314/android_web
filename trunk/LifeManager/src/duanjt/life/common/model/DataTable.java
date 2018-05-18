package duanjt.life.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ģ��ʵ��DataTable��ع���
 * 
 * @author �ν���
 *
 */
public class DataTable extends ArrayList<DataRow> {
	/**
	 * ��ǰ�����Ƿ�Ϊ�ջ���������Ϊ0
	 * 
	 * @return
	 */
	public boolean isNullOrEmpty() {
		if (this == null || this.size() == 0)
			return true;
		return false;
	}

	/**
	 * ����еļ���
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

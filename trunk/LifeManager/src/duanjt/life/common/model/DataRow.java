package duanjt.life.common.model;

import java.util.LinkedHashMap;

/**
 * 数据行
 * @author Administrator
 * HashMap是无序的，LinkedHashMap是有序的(取数据时先插入先取出)
 */
public class DataRow extends LinkedHashMap<String, Object>{

	@Override
	public Object put(String key, Object value) {
		// TODO Auto-generated method stub
		return super.put(key.toUpperCase(), value);
	}

	@Override
	public Object get(Object key) {
		// TODO Auto-generated method stub
		return super.get(key.toString().toUpperCase());
	}
	
	

}

package duanjt.life.common.model;

import java.util.LinkedHashMap;

/**
 * ������
 * @author Administrator
 * HashMap������ģ�LinkedHashMap�������(ȡ����ʱ�Ȳ�����ȡ��)
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

package duanjt.life.model;

import java.util.Date;

/**
 * ������������Ƽ���
 * 
 * @author duanjt
 * 
 */
public class LifeNames {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

	protected String id;
	protected String name;

	public LifeNames() {
	}

	// ��� ��� ��� 10λ���ı��
	public String getId() {
		return id;
	}

	public void setId(String value) {
		this.id = value;
	}

	// �ֵ�����
	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}


}

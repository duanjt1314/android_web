package duanjt.life.model;

import java.util.Date;

/**
 * �û�ʵ����
 * 
 * @author duanjt
 * 
 */
public class Diction {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

	protected String id;
	protected String name;
	protected String note;
	protected int parentId;
	protected int orderId;

	public Diction() {
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

	// ��ע
	public String getNote() {
		return note;
	}

	public void setNote(String value) {
		this.note = value;
	}

	// ������� ������� ������� ��������
	public int getParentId() {
		return parentId;
	}

	public void setParentId(int value) {
		this.parentId = value;
	}

	// ���
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int value) {
		this.orderId = value;
	}


}

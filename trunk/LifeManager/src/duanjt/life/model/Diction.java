package duanjt.life.model;

import java.util.Date;

/**
 * 用户实体类
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

	// 编号 编号 编号 10位数的编号
	public String getId() {
		return id;
	}

	public void setId(String value) {
		this.id = value;
	}

	// 字典名称
	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	// 备注
	public String getNote() {
		return note;
	}

	public void setNote(String value) {
		this.note = value;
	}

	// 父级编号 父级编号 父级编号 引用主键
	public int getParentId() {
		return parentId;
	}

	public void setParentId(int value) {
		this.parentId = value;
	}

	// 序号
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int value) {
		this.orderId = value;
	}


}

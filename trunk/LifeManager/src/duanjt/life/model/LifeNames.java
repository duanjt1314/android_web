package duanjt.life.model;

import java.util.Date;

/**
 * 生活费消费名称集合
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


}

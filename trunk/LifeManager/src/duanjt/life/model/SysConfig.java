package duanjt.life.model;

/**
 * 系统配置实体对象
 * 
 * @author Administrator
 * 
 */
public class SysConfig {
	protected String id;
	protected String sysKey;
	protected String sysValue;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSysKey() {
		return sysKey;
	}
	public void setSysKey(String sysKey) {
		this.sysKey = sysKey;
	}
	public String getSysValue() {
		return sysValue;
	}
	public void setSysValue(String sysValue) {
		this.sysValue = sysValue;
	}

}

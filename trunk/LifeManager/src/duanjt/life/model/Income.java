package duanjt.life.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * 纯收入信息
 * @author Administrator
 *
 */
public class Income {
	@SerializedName("Id")
	private String id;
	@SerializedName("Time")
	private String time;
	@SerializedName("Price")
	private String price;
	@SerializedName("Note")
	private String note;
	@SerializedName("CreateBy")
	private String createBy;
	@SerializedName("CreateTime")
	private String createTime;
	@SerializedName("UpdateBy")
	private String updateBy;
	@SerializedName("UpdateTime")
	private String updateTime;	
	@SerializedName("FamilyIncome")
	private String familyIncome;
	@SerializedName("IsMark")
	private String isMark;
	@SerializedName("CusGroup")
	private String cusGroup;
	@SerializedName("IsUpload")
	private String isUpload;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getFamilyIncome() {
		return familyIncome;
	}
	public void setFamilyIncome(String familyIncome) {
		this.familyIncome = familyIncome;
	}
	public String getIsMark() {
		return isMark;
	}
	public void setIsMark(String isMark) {
		this.isMark = isMark;
	}
	public String getCusGroup() {
		return cusGroup;
	}
	public void setCusGroup(String cusGroup) {
		this.cusGroup = cusGroup;
	}
	public String getIsUpload() {
		return isUpload;
	}
	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}
	
}

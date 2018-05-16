package duanjt.life.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * 生活消费信息
 * 
 * @author Administrator
 * 
 */
public class LifingCost {
	@SerializedName("Id")
	private String id;
	@SerializedName("Time")
	private String time;
	@SerializedName("Reason")
	private String reason;
	@SerializedName("Price")
	private String price;
	@SerializedName("CostTypeId")
	private String costTypeId;
	@SerializedName("Notes")
	private String notes;
	@SerializedName("ImgUrl")
	private String imgUrl;
	@SerializedName("IsMark")
	private String isMark;
	@SerializedName("FamilyPay")
	private String familyPay;
	@SerializedName("CreateBy")
	private String createBy;
	@SerializedName("CreateTime")
	private String createTime;
	@SerializedName("UpdateBy")
	private String updateBy;
	@SerializedName("UpdateTime")
	private String updateTime;
	@SerializedName("CusGroup")
	private String cusGroup;
	@SerializedName("IsUpload")
	private String isUpload;
	@SerializedName("CostTypeName")
	private String costTypeName;

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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCostTypeId() {
		return costTypeId;
	}

	public void setCostTypeId(String costTypeId) {
		this.costTypeId = costTypeId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIsMark() {
		return isMark;
	}

	public void setIsMark(String isMark) {
		this.isMark = isMark;
	}

	public String getFamilyPay() {
		return familyPay;
	}

	public void setFamilyPay(String familyPay) {
		this.familyPay = familyPay;
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

	public String getCusGroup() {
		return cusGroup;
	}

	public void setCusGroup(String cusGroup) {
		this.cusGroup = cusGroup;
	}

	public void setCostTypeName(String costTypeName) {
		this.costTypeName = costTypeName;
	}

	public String getCostTypeName() {
		return costTypeName;
	}

	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

}

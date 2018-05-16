package duanjt.life.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * 银行卡信息
 * 
 * @author Administrator
 * 
 */
public class BankCard {
	@SerializedName("Id")
	private String id;
	@SerializedName("Time")
	private String time;
	@SerializedName("Price")
	private String price;
	@SerializedName("SaveType")
	private String saveType;
	@SerializedName("Balance")
	private String balance;
	@SerializedName("BankType")
	private String bankType;
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
	@SerializedName("ImgUrl")
	private String imgUrl;
	@SerializedName("IsUpload")
	private String isUpload;
	@SerializedName("BankTypeName")
	private String bankTypeName;

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

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public String getBankTypeName() {
		return bankTypeName;
	}

	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}
}

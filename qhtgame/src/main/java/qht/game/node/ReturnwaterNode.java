package qht.game.node;

import java.math.BigDecimal;

public class ReturnwaterNode {
	private long id;
	private int date;
	private String appcode;
	private String username;
	private int typeid;
	private BigDecimal water_amount;
	private BigDecimal profit_amount;
	private BigDecimal up_amount;
	private BigDecimal down_amount;
	private int status;
	private BigDecimal return_amount;
	private long updatetime;
	private long createtime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public BigDecimal getWater_amount() {
		return water_amount;
	}
	public void setWater_amount(BigDecimal water_amount) {
		this.water_amount = water_amount;
	}
	public BigDecimal getProfit_amount() {
		return profit_amount;
	}
	public void setProfit_amount(BigDecimal profit_amount) {
		this.profit_amount = profit_amount;
	}
	public BigDecimal getUp_amount() {
		return up_amount;
	}
	public void setUp_amount(BigDecimal up_amount) {
		this.up_amount = up_amount;
	}
	public BigDecimal getDown_amount() {
		return down_amount;
	}
	public void setDown_amount(BigDecimal down_amount) {
		this.down_amount = down_amount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BigDecimal getReturn_amount() {
		return return_amount;
	}
	public void setReturn_amount(BigDecimal return_amount) {
		this.return_amount = return_amount;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
}

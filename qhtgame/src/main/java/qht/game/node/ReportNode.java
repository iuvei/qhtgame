package qht.game.node;

import java.math.BigDecimal;

public class ReportNode {
	private long id;
	private int date;
	private String appcode;
	private String username;
	private String loginname;
	private int typeid;
	private int recharge_count;
	private BigDecimal recharge_amount;
	private int withdrawals_count;
	private BigDecimal withdrawals_amount;
	private int game_count;
	private BigDecimal bet_amount;
	private BigDecimal paid_amount;
	private long updatetime;
	
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
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
	public int getRecharge_count() {
		return recharge_count;
	}
	public void setRecharge_count(int recharge_count) {
		this.recharge_count = recharge_count;
	}
	public BigDecimal getRecharge_amount() {
		return recharge_amount;
	}
	public void setRecharge_amount(BigDecimal recharge_amount) {
		this.recharge_amount = recharge_amount;
	}
	public int getWithdrawals_count() {
		return withdrawals_count;
	}
	public void setWithdrawals_count(int withdrawals_count) {
		this.withdrawals_count = withdrawals_count;
	}
	public BigDecimal getWithdrawals_amount() {
		return withdrawals_amount;
	}
	public void setWithdrawals_amount(BigDecimal withdrawals_amount) {
		this.withdrawals_amount = withdrawals_amount;
	}
	public int getGame_count() {
		return game_count;
	}
	public void setGame_count(int game_count) {
		this.game_count = game_count;
	}
	public BigDecimal getBet_amount() {
		return bet_amount;
	}
	public void setBet_amount(BigDecimal bet_amount) {
		this.bet_amount = bet_amount;
	}
	public BigDecimal getPaid_amount() {
		return paid_amount;
	}
	public void setPaid_amount(BigDecimal paid_amount) {
		this.paid_amount = paid_amount;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
}

package qht.game.node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PeriodRecord {
	private long id;
	private String period;
	private String appcode;
	private int lorreyid;
	private int number;
	private BigDecimal bet;
	private BigDecimal paid;
	private long updatetime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public int getLorreyid() {
		return lorreyid;
	}
	public void setLorreyid(int lorreyid) {
		this.lorreyid = lorreyid;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public BigDecimal getBet() {
		return bet;
	}
	public void setBet(BigDecimal bet) {
		this.bet = bet;
	}
	public BigDecimal getPaid() {
		return paid;
	}
	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public Map<String,Object> getParameter() {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("period", period);
		parameter.put("appcode", appcode);
		parameter.put("lorreyid", lorreyid);
		parameter.put("number", number);
		parameter.put("bet", bet);
		parameter.put("paid", paid);
		parameter.put("updatetime", updatetime);
		return parameter;
	}
}

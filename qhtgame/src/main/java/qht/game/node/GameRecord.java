package qht.game.node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GameRecord {
	private long id;
	private int lotteryid;
	private String period;
	private String appcode;
	private String username;
	private int typeid;
	private BigDecimal betamount;
	private BigDecimal paidamount;
	private long updatetime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(int lotteryid) {
		this.lotteryid = lotteryid;
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
	public BigDecimal getBetamount() {
		return betamount;
	}
	public void setBetamount(BigDecimal betamount) {
		this.betamount = betamount;
	}
	public BigDecimal getPaidamount() {
		return paidamount;
	}
	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public Map<String, Object> getParameter() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("id", id);
		parameter.put("lotteryid", lotteryid);
		parameter.put("period", period);
		parameter.put("appcode", appcode);
		parameter.put("username", username);
		parameter.put("typeid", typeid);
		parameter.put("betamount", betamount);
		parameter.put("paidamount", paidamount);
		parameter.put("updatetime", updatetime);
		
		return parameter;
	}
}

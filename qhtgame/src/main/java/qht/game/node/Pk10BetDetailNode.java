package qht.game.node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Pk10BetDetailNode {

	private long id;
	private String odd;
	private String period;
	private String agent;
	private String appcode;
	private String username;
	private int type;
	private BigDecimal amount;
	private byte[] detail;
	private long updatetime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOdd() {
		return odd;
	}
	public void setOdd(String odd) {
		this.odd = odd;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
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
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public byte[] getDetail() {
		return detail;
	}
	public void setDetail(byte[] detail) {
		this.detail = detail;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Map<String, Object> getParameter() {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("odd", odd);
		parameter.put("agent", agent);
		parameter.put("appcode", appcode);
		parameter.put("username", username);
		parameter.put("type", type);
		parameter.put("period", period);
		parameter.put("amount", amount);
		parameter.put("detail", detail);
		parameter.put("updatetime", updatetime);
		return parameter;
	}
}

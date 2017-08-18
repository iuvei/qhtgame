package qht.game.node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AmountRecord {
	private long id;
	private String appcode;
	private String username;
	private int type;
	private BigDecimal amount;
	private BigDecimal bef_bal;
	private BigDecimal aft_bal;
	private long updatetime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getBef_bal() {
		return bef_bal;
	}
	public void setBef_bal(BigDecimal bef_bal) {
		this.bef_bal = bef_bal;
	}
	public BigDecimal getAft_bal() {
		return aft_bal;
	}
	public void setAft_bal(BigDecimal aft_bal) {
		this.aft_bal = aft_bal;
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
		parameter.put("appcode", appcode);
		parameter.put("username", username);
		parameter.put("type", type);
		parameter.put("amount", amount);
		parameter.put("bef_bal", bef_bal);
		parameter.put("aft_bal", aft_bal);
		parameter.put("updatetime", updatetime);
		
		return parameter;
	}
}

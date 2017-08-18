package qht.game.node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FinanceRecord {
	private long id;
	private String appcode;
	private String username;
	private int type;
	private int typeid;
	private BigDecimal amount;
	private String requestname;
	private String oprname;
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
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getRequestname() {
		return requestname;
	}
	public void setRequestname(String requestname) {
		this.requestname = requestname;
	}
	public String getOprname() {
		return oprname;
	}
	public void setOprname(String oprname) {
		this.oprname = oprname;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public Map<String, Object> getParameter() {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id",id);
		parameter.put("appcode",appcode);
		parameter.put("username",username);
		parameter.put("type",type);
		parameter.put("typeid",typeid);
		parameter.put("amount",amount);
		parameter.put("requestname",requestname);
		parameter.put("oprname",oprname);
		parameter.put("updatetime",updatetime);
		return parameter;
	}
}

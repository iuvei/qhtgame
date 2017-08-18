package qht.game.node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FinanceOrderNode {
	private long id;
	private String odd;
	private String username;
	private int type;
	private BigDecimal amount;
	private long requesttime;
	private String requestname;
	private String nickname;
	private String typeid;
	private int tag;
	private long updatetime;
//	private BigDecimal AMUP;
//	private BigDecimal AMDOWN;
	
	
//	public BigDecimal getAMUP() {
//		return AMUP;
//	}
//	public void setAMUP(BigDecimal aMUP) {
//		AMUP = aMUP;
//	}
//	public BigDecimal getAMDOWN() {
//		return AMDOWN;
//	}
//	public void setAMDOWN(BigDecimal aMDOWN) {
//		AMDOWN = aMDOWN;
//	}
	
	public long getId() {
		return id;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public long getRequesttime() {
		return requesttime;
	}
	public void setRequesttime(long requesttime) {
		this.requesttime = requesttime;
	}
	public String getRequestname() {
		return requestname;
	}
	public void setRequestname(String requestname) {
		this.requestname = requestname;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	
	public Map<String, Object> getParameter() {
		Map<String,Object> parameter = new HashMap<String,Object>();
//		private long id;
//		private String odd;
//		private String username;
//		private int type;
//		private BigDecimal amount;
//		private long requesttime;
//		private String requestname;
//		private int tag;
//		private long updatetime;
		parameter.put("id",id);
		parameter.put("odd",odd);
		parameter.put("username",username);
		parameter.put("type",type);
		parameter.put("amount",amount);
		parameter.put("requesttime",requesttime);
		parameter.put("requestname",requestname);
		parameter.put("nickname",nickname);
		parameter.put("tag",tag);
		parameter.put("updatetime",updatetime);
		return parameter;
	}
}

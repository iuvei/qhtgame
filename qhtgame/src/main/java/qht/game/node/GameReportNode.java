package qht.game.node;

import java.math.BigDecimal;

public class GameReportNode {
	private String appcode;
	private String username;
	private int typeid;
	private int count;
	private BigDecimal sumBet;
	private BigDecimal sumPaid;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public BigDecimal getSumBet() {
		return sumBet;
	}
	public void setSumBet(BigDecimal sumBet) {
		this.sumBet = sumBet;
	}
	public BigDecimal getSumPaid() {
		return sumPaid;
	}
	public void setSumPaid(BigDecimal sumPaid) {
		this.sumPaid = sumPaid;
	}
}

package com.node;

import java.math.BigDecimal;

public class PlayerInfo {
	private String username;
	private String nickname;
	private int status;
	private BigDecimal balance;
	private BigDecimal frozen_bal;
	private BigDecimal integral;
	private long createtime;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getFrozen_bal() {
		return frozen_bal;
	}
	public void setFrozen_bal(BigDecimal frozen_bal) {
		this.frozen_bal = frozen_bal;
	}
	public BigDecimal getIntegral() {
		return integral;
	}
	public void setIntegral(BigDecimal integral) {
		this.integral = integral;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
}

package com.node;

import java.math.BigDecimal;

import com.util.CoinUtil;

public class Pk10CurNode {
	private String username;
	private BigDecimal balance;
	private BigDecimal amount;		//当局累计
	private String period;
	private int sealtime;
	private int opentime;
	public Pk10CurNode() {
		super();
		this.username = "";
		this.balance = CoinUtil.zero;
		this.amount = CoinUtil.zero;
		this.period = "";
		this.sealtime = 0;
		this.opentime = 0;
	}
	public void clear() {
		this.username = "";
		this.balance = CoinUtil.zero;
		this.amount = CoinUtil.zero;
		this.period = "";
		this.sealtime = 0;
		this.opentime = 0;	
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public int getSealtime() {
		return sealtime;
	}
	public void setSealtime(int sealtime) {
		this.sealtime = sealtime;
	}
	public int getOpentime() {
		return opentime;
	}
	public void setOpentime(int opentime) {
		this.opentime = opentime;
	}
}

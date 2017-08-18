package com.pk10.node;

public class Pk10BetStr {
	private String strRunway;
	private String strContent;
	private String strMoney;
	public Pk10BetStr(String strRunway, String strContent, String strMoney) {
		super();
		this.strRunway = strRunway;
		this.strContent = strContent;
		this.strMoney = strMoney;
	}
	public String getStrRunway() {
		return strRunway;
	}
	public String getStrContent() {
		return strContent;
	}
	public String getStrMoney() {
		return strMoney;
	}
}

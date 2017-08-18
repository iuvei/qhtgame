package com.pk10.node;

import java.math.BigDecimal;
import java.util.List;

public class Pk10BetTextNode {

	private List<Integer> arrRunway;
	private List<Integer> arrContent;
	private BigDecimal money;
	public Pk10BetTextNode(List<Integer> arrRunway, List<Integer> arrContent, BigDecimal money) {
		super();
		this.arrRunway = arrRunway;
		this.arrContent = arrContent;
		this.money = money;
	}
	public List<Integer> getArrRunway() {
		return arrRunway;
	}
	public List<Integer> getArrContent() {
		return arrContent;
	}
	public BigDecimal getMoney() {
		return money;
	}
}


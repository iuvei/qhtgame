package com.pk10.node;

import java.math.BigDecimal;
import java.util.List;

public class Pk10BetText {

	private int runway;
	private List<String> listContent;
	private BigDecimal money;
	public Pk10BetText(int runway, List<String> listContent, BigDecimal money) {
		super();
		this.runway = runway;
		this.listContent = listContent;
		this.money = money;
	}
	public int getRunway() {
		return runway;
	}
	public List<String> getListContent() {
		return listContent;
	}
	public BigDecimal getMoney() {
		return money;
	}
}

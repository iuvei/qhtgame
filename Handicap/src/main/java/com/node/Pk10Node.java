package com.node;

import java.math.BigDecimal;

public class Pk10Node {

	private int index;
	private BigDecimal amount;
	public Pk10Node(int index, BigDecimal amount) {
		super();
		this.index = index;
		this.amount = amount;
	}
	public int getIndex() {
		return index;
	}
	public BigDecimal getAmount() {
		return amount;
	}
}

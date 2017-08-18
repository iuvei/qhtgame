package com.node;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.util.CoinUtil;

public class HandicapLitNode {
	private BigDecimal allBet;
	private List<Pk10Info> arrList;
	public HandicapLitNode() {
		allBet = CoinUtil.zero;
		arrList = new ArrayList<Pk10Info>();
	}
	public BigDecimal getAllBet() {
		return allBet;
	}
	public void setAllBet(BigDecimal allBet) {
		this.allBet = allBet;
	}
	public List<Pk10Info> getArrList() {
		return arrList;
	}
	public void setArrList(List<Pk10Info> arrList) {
		this.arrList = arrList;
	}
	public void addOneList(Pk10Info info) {
		arrList.add(info);
	}
}

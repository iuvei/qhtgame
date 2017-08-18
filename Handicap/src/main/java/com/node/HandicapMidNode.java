package com.node;

import java.util.List;

public class HandicapMidNode {
	private String ctypep;
	private List<Pk10Info> arrData;
	public HandicapMidNode(String ctypep, List<Pk10Info> arrData) {
		super();
		this.ctypep = ctypep;
		this.arrData = arrData;
	}
	public String getCtypep() {
		return ctypep;
	}
	public void setCtypep(String ctypep) {
		this.ctypep = ctypep;
	}
	public List<Pk10Info> getArrData() {
		return arrData;
	}
	public void setArrData(List<Pk10Info> arrData) {
		this.arrData = arrData;
	}
}

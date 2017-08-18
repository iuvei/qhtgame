package com.pk10.node;

import java.util.List;

public class Pk10PeriodDataDayList {

	private int rows;
	private String code;
	private String remain;
	private List<Pk10PeriodDataDayNode> data;
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRemain() {
		return remain;
	}
	public void setRemain(String remain) {
		this.remain = remain;
	}
	public List<Pk10PeriodDataDayNode> getData() {
		return data;
	}
	public void setData(List<Pk10PeriodDataDayNode> data) {
		this.data = data;
	}
}

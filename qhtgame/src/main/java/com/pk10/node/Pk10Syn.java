package com.pk10.node;

import java.util.List;

public class Pk10Syn {
	private int rows;
	private String code;
	private String remain;
	private List<Pk10SynNext> next;
	private List<Pk10SynPeriod> open;
	private String time;
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
	public List<Pk10SynNext> getNext() {
		return next;
	}
	public void setNext(List<Pk10SynNext> next) {
		this.next = next;
	}
	public List<Pk10SynPeriod> getOpen() {
		return open;
	}
	public void setOpen(List<Pk10SynPeriod> open) {
		this.open = open;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}

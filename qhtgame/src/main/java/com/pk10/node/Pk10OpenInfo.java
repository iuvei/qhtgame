package com.pk10.node;

import java.util.List;

public class Pk10OpenInfo {
	private long localtime;	//当地时间
	private long timestamp;
	private String nextPeriod;
	private long nextOpentime;
	private String period;
	private long opentime;
	private List<Integer> opencode;
	public long getLocaltime() {
		return localtime;
	}
	public void setLocaltime(long localtime) {
		if (this.localtime<localtime) {
			long tm = localtime - this.localtime;
			timestamp += tm;
		}
		this.localtime = localtime;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getNextPeriod() {
		return nextPeriod;
	}
	public void setNextPeriod(String nextPeriod) {
		this.nextPeriod = nextPeriod;
	}
	public long getNextOpentime() {
		return nextOpentime;
	}
	public void setNextOpentime(long nextOpentime) {
		this.nextOpentime = nextOpentime;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public long getOpentime() {
		return opentime;
	}
	public void setOpentime(long opentime) {
		this.opentime = opentime;
	}
	public List<Integer> getOpencode() {
		return opencode;
	}
	public void setOpencode(List<Integer> opencode) {
		this.opencode = opencode;
	}
}

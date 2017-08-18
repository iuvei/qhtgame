package com.pk10.node;

import java.util.List;

public class Pk10OpenNode {
	private long timestamp;
	private long sealplatTime;
	private String nextPeriod;
	private long nextOpentime;
	private String period;
	private long opentime;
	private List<Integer> opencode;
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getSealplatTime() {
		return sealplatTime;
	}
	public void setSealplatTime(long sealplatTime) {
		this.sealplatTime = sealplatTime;
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

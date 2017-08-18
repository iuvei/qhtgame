package com.pk10.node;

import java.sql.Timestamp;

public class Pk10Data {

	private int id;
	private String period;
	private Timestamp lottery_time;
	private String lottery_data;
	private Timestamp collection_time;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Timestamp getLottery_time() {
		return lottery_time;
	}
	public void setLottery_time(Timestamp lottery_time) {
		this.lottery_time = lottery_time;
	}
	public String getLottery_data() {
		return lottery_data;
	}
	public void setLottery_data(String lottery_data) {
		this.lottery_data = lottery_data;
	}
	public Timestamp getCollection_time() {
		return collection_time;
	}
	public void setCollection_time(Timestamp collection_time) {
		this.collection_time = collection_time;
	}
	@Override
	public String toString() {
		return "{id:" + id + ",period:" + period + ",lottery_time:" + lottery_time + ",lottery_data:"
				+ lottery_data + ",collection_time:" + collection_time + "}";
	}

	
	
	
	
	
	
}


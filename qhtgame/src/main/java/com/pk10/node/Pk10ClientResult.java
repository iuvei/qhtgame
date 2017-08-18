package com.pk10.node;

public class Pk10ClientResult {

	private int curTime;	//当前时间(秒)
	private String pre_period;	//上期期号
	private String pre_data;	//上期开奖数据
	private int pre_time;	//上期开奖时间(秒)
	private String cur_period;	//当期期号
	private int cur_time;	//当期开奖时间(秒)
	private int seal_plate;	//封盘时间(秒) 距离开盘时间
	public int getCurTime() {
		return curTime;
	}
	public void setCurTime(int curTime) {
		this.curTime = curTime;
	}
	public String getPre_period() {
		return pre_period;
	}
	public void setPre_period(String pre_period) {
		this.pre_period = pre_period;
	}
	public String getPre_data() {
		return pre_data;
	}
	public void setPre_data(String pre_data) {
		this.pre_data = pre_data;
	}
	public int getPre_time() {
		return pre_time;
	}
	public void setPre_time(int pre_time) {
		this.pre_time = pre_time;
	}
	public String getCur_period() {
		return cur_period;
	}
	public void setCur_period(String cur_period) {
		this.cur_period = cur_period;
	}
	public int getCur_time() {
		return cur_time;
	}
	public void setCur_time(int cur_time) {
		this.cur_time = cur_time;
	}
	public int getSeal_plate() {
		return seal_plate;
	}
	public void setSeal_plate(int seal_plate) {
		this.seal_plate = seal_plate;
	}
	
	
}

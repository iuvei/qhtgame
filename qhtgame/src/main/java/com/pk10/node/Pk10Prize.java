package com.pk10.node;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Pk10Prize {

	private Timestamp time_stamp;	//当前时间
	private String pri_period;	//上期期号
	private String pri_data;	//上期开奖数据
	private Timestamp pri_time;	//上期开奖时间
	private String cur_period;	//当期期号
	private Timestamp cur_time;	//当期开奖时间

	public Timestamp getTime_stamp() {
		return time_stamp;
	}
	public void setTime_stamp(Timestamp time_stamp) {
		this.time_stamp = time_stamp;
	}
	public String getPri_period() {
		return pri_period;
	}
	public void setPri_period(String pri_period) {
		this.pri_period = pri_period;
	}
	public String getPri_data() {
		return pri_data;
	}
	public void setPri_data(String pri_data) {
		this.pri_data = pri_data;
	}
	public Timestamp getPri_time() {
		return pri_time;
	}
	public void setPri_time(Timestamp pri_time) {
		this.pri_time = pri_time;
	}
	public String getCur_period() {
		return cur_period;
	}
	public void setCur_period(String cur_period) {
		this.cur_period = cur_period;
	}
	public Timestamp getCur_time() {
		return cur_time;
	}
	public void setCur_time(Timestamp cur_time) {
		this.cur_time = cur_time;
	}
	/*@Override
	public String toString() {
		return "{'id':" + id + ",'time_stamp':'" + DateUtil.TimestampToString(time_stamp, "yyyy/MM/dd HH:mm:ss") + "','pri_period':'" + pri_period + "','pri_data':'"
				+ pri_data + "','pri_time':'" + DateUtil.TimestampToString(pri_time, "yyyy/MM/dd HH:mm:ss") + "','cur_period':'" + cur_period + "','cur_time':'" + DateUtil.TimestampToString(cur_time, "yyyy/MM/dd HH:mm:ss") + "'}";
	}*/
	public Map<String, Object> getParameter() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("period", pri_period);
		parameter.put("data", pri_data);
		parameter.put("opentime", pri_time.getTime());
		parameter.put("updatetime", time_stamp.getTime());
		return parameter;
	}

	
	
}

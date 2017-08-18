package net.node;

import java.util.HashMap;
import java.util.Map;

import com.util.DateUtil;

public class LotterySetTimeCSNode {
	private int id;
	private long opentime;
	private long spacetime;
	private long notbettime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getOpentime() {
		return opentime;
	}
	public void setOpentime(long opentime) {
		this.opentime = opentime;
	}
	public long getSpacetime() {
		return spacetime;
	}
	public void setSpacetime(long spacetime) {
		this.spacetime = spacetime;
	}
	public long getNotbettime() {
		return notbettime;
	}
	public void setNotbettime(long notbettime) {
		this.notbettime = notbettime;
	}
	public Map<String, Object> getParameter() {
		if (id<=0 || spacetime<=0)
			return null;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("opentime", opentime);
		parameter.put("spacetime", spacetime);
		parameter.put("notbettime", notbettime);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
}

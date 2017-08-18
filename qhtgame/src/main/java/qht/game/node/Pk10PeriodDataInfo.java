package qht.game.node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Pk10PeriodDataInfo {

	private String period;
	private List<Integer> opencode;
	private long opentime;
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public List<Integer> getOpencode() {
		return opencode;
	}
	public void setOpencode(List<Integer> opencode) {
		this.opencode = opencode;
	}
	
	public long getOpentime() {
		return opentime;
	}
	public void setOpentime(long opentime) {
		this.opentime = opentime;
	}
	public Map<String, Object> getParameter() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("period", period);
		parameter.put("opencode", new Gson().toJson(opencode));
		parameter.put("opentime", opentime);
		return parameter;
	}
}

package net.node;

import java.util.HashMap;
import java.util.Map;

import com.util.ComUtil;
import com.util.DateUtil;

public class LotteryInsetCSNode {
	private int id;
	private String name;
	private String shutname;
	private long opentime;
	private long spacetime;
	private long notbettime;
	private String rule;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShutname() {
		return shutname;
	}

	public void setShutname(String shutname) {
		this.shutname = shutname;
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

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public Map<String, Object> getParameter() {
		if ( spacetime<=0)
			return null;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("name", name);
		parameter.put("id", id);
		parameter.put("shutname", shutname);
		parameter.put("opentime", opentime);
		parameter.put("spacetime", spacetime);
		parameter.put("notbettime", notbettime);
		parameter.put("rule", ComUtil.encode(rule.getBytes()));
		parameter.put("createtime", DateUtil.getCurTimestamp().getTime());
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
}

package net.node;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.util.DateUtil;

public class AppinfoSetTimeCSNode {
	private String appcode;
	private String actiontime;	//YYYY-MM-DD HH:mm:ss
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public String getActiontime() {
		return actiontime;
	}
	public void setActiontime(String actiontime) {
		this.actiontime = actiontime;
	}
	public Map<String, Object> getParameter() {
		if (	appcode==null || appcode.length()==0 || 
				actiontime==null || actiontime.length()==0)
			return null;
		
		Timestamp timestamp = DateUtil.StringToTimestamp(actiontime);
		if (timestamp==null)
			return null;
		long lactiontime = timestamp.getTime();
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", appcode);
		parameter.put("spacetime", lactiontime);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
}

package net.node;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.util.DateUtil;

public class AppinfoInsertCSNode {
	private String appcode;
	private String agent;
	private String appname;
	private String appcompany;
	private String actiontime;	//YYYY-MM-DD HH:mm:ss
	private String wechat_code;
	private String wechat_p;
	private String wechat_img;
	
	
	public String getWechat_img() {
		return wechat_img;
	}
	public void setWechat_img(String wechat_img) {
		this.wechat_img = wechat_img;
	}
	public String getWechat_code() {
		return wechat_code;
	}
	public void setWechat_code(String wechat_code) {
		this.wechat_code = wechat_code;
	}
	public String getWechat_p() {
		return wechat_p;
	}
	public void setWechat_p(String wechat_p) {
		this.wechat_p = wechat_p;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getAppcompany() {
		return appcompany;
	}
	public void setAppcompany(String appcompany) {
		this.appcompany = appcompany;
	}
	public String getActiontime() {
		return actiontime;
	}
	public void setActiontime(String actiontime) {
		this.actiontime = actiontime;
	}
	
	public Map<String, Object> getParameter() {
		if (	appcode==null || appcode.length()==0 || 
				agent==null || agent.length()==0 ||
				appname==null || appname.length()==0 ||
				appcompany==null || appcompany.length()==0 ||
				wechat_code==null || wechat_code.length()==0 ||
				wechat_p==null || wechat_p.length()==0 ||
						wechat_img==null || wechat_img.length()==0 ||
				actiontime==null || actiontime.length()==0)
			return null;
		
		Timestamp timestamp = DateUtil.StringToTimestamp(actiontime);
		if (timestamp==null)
			return null;
		long lactiontime = timestamp.getTime();
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", appcode);
		parameter.put("agent", agent);
		parameter.put("appname", appname);
		parameter.put("appcompany", appcompany);
		parameter.put("actiontime", lactiontime);
		parameter.put("wechat_code", wechat_code);
		parameter.put("wechat_p", wechat_img);
		parameter.put("wechat_img", wechat_img);
		parameter.put("createtime", DateUtil.getCurTimestamp().getTime());
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
	
}

package net.node;

import java.util.HashMap;
import java.util.Map;

import com.util.DateUtil;

public class AppinfoEditwcCSNode {
	private String appcode;
	private String wechat_code;
	private String wechat_p;
	
	public String getAppcode() {
		return appcode;
	}

	public void setAppcode(String appcode) {
		this.appcode = appcode;
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

	public Map<String, Object> getParameter() {
		if (	appcode==null || appcode.length()==0 || 
				wechat_p==null || wechat_p.length()==0 || 
				wechat_code==null || wechat_code.length()==0)
			return null;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", appcode);
		parameter.put("wechat_code", wechat_code);
		parameter.put("wechat_p", wechat_p);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
}

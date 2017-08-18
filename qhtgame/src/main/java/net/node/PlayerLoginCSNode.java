package net.node;

import java.util.HashMap;
import java.util.Map;

public class PlayerLoginCSNode {

	private String appcode;
	private String username;
	private String password;
	
	public String getAppcode() {
		return appcode;
	}

	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String,Object> getParameter() {
		if (	appcode==null || appcode.length()==0 || appcode.length()>31 ||
				username==null || username.length()==0 || username.length()>31 ||
				password==null || password.length()==0 || password.length()>127)
			return null;

		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", appcode);
		parameter.put("username", username);
		parameter.put("password", password);
		
		return parameter;
	}
}

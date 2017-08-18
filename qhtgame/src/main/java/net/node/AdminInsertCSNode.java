package net.node;

import java.util.HashMap;
import java.util.Map;

import com.sysconst.Code;
import com.util.DateUtil;

public class AdminInsertCSNode {
	
	private String username;
	private String nickname;
	private String password;
	private int type;
	private int status;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Map<String,Object> getParameter() {
		if (	username==null || username.length()>31 ||
				nickname==null || nickname.length()>31 ||
				password==null || password.length()>31 ||
				(type!=Code.ADMIN_AGENT && type!=Code.ADMIN_APP) ||
				(status!=Code.STATUS_EFFECTIVE && status!=Code.STATUS_UNEFFECTIVE) )
			return null;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		parameter.put("nickname", nickname);
		parameter.put("password", password);
		parameter.put("type", type);
		parameter.put("status", status);
		parameter.put("jurisdiction", "0");
		parameter.put("createtime", DateUtil.getCurTimestamp().getTime());
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		
		
		return parameter;
	}
}

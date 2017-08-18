package net.node;

import java.util.HashMap;
import java.util.Map;

import com.util.DateUtil;

public class AdminResetPasswordCSNode {

	private int id;
	private String newpassword;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNewpassword() {
		return newpassword;
	}
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	public Map<String, Object> getParameter() {
		if (id<=0 || newpassword==null || newpassword.length()==0)
			return null;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("password", newpassword);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
}

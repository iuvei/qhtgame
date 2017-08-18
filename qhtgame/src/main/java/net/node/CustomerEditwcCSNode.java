package net.node;

import java.util.HashMap;
import java.util.Map;

import com.util.DateUtil;

public class CustomerEditwcCSNode {
	private int id;
	private String cus_num;
	private String cus_pwd;
	private String cus_name;
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCus_num() {
		return cus_num;
	}


	public void setCus_num(String cus_num) {
		this.cus_num = cus_num;
	}


	public String getCus_pwd() {
		return cus_pwd;
	}


	public void setCus_pwd(String cus_pwd) {
		this.cus_pwd = cus_pwd;
	}


	public String getCus_name() {
		return cus_name;
	}


	public void setCus_name(String cus_name) {
		this.cus_name = cus_name;
	}

	public Map<String, Object> getParameter() {
		if (	cus_num==null || cus_num.length()==0 || 
				cus_pwd==null || cus_pwd.length()==0 || 
						cus_name==null || cus_name.length()==0)
			return null;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("cus_num", cus_num);
		parameter.put("cus_pwd", cus_pwd);
		parameter.put("cus_name", cus_name);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
}

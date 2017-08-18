package net.node;

import java.util.HashMap;
import java.util.Map;

import com.util.DateUtil;

public class AdminEditNicknameCSNode {
	private int id;
	private String newnickname;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNewnickname() {
		return newnickname;
	}
	public void setNewnickname(String newnickname) {
		this.newnickname = newnickname;
	}
	
	public Map<String, Object> getParameter() {
		if (id<=0 || newnickname==null || newnickname.length()==0)
			return null;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("nickname", newnickname);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
}

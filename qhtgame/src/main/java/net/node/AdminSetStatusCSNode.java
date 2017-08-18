package net.node;

import java.util.HashMap;
import java.util.Map;

import com.sysconst.Code;
import com.util.DateUtil;

public class AdminSetStatusCSNode {
	private int id;
	private int status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<String, Object> getParameter() {
		if (id<=0 || (status!=Code.STATUS_EFFECTIVE && status!=Code.STATUS_UNEFFECTIVE))
			return null;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("status", status);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}
}

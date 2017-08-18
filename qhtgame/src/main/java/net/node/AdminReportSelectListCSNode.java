package net.node;

import java.util.HashMap;
import java.util.Map;

import com.sysconst.Code;

public class AdminReportSelectListCSNode {

	private String username;
	private String loginname;
	private int begindate;
	private int enddate;
	private int page;
	private int count;
//	private String username;
//	private String loginname;
//	private String begindate;
//	private String enddate;
//	private int page;
//	private int count;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public int getBegindate() {
		return begindate;
	}

	public void setBegindate(int begindate) {
		this.begindate = begindate;
	}

	public int getEnddate() {
		return enddate;
	}

	public void setEnddate(int enddate) {
		this.enddate = enddate;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	public Map<String, Object> getParameter() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		if (username==null ||loginname==null || begindate<=0 || enddate<=0 || begindate>enddate)
			return null;
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		parameter.put("username", username);
		parameter.put("loginname", loginname);
		parameter.put("begindate", begindate);
		parameter.put("enddate", enddate);
		return parameter;
//		if (	username==null || username.length()>31 ||
//				loginname==null || loginname.length()>31 ||
//						begindate==null ||
//								enddate==null)
//			return null;
//		Timestamp _begin = DateUtil.StringToTimestamp(begindate);
//		Timestamp _end = DateUtil.StringToTimestamp(enddate);
//		if (_begin==null || _end==null)
//			return null;
//		long lbegintime = _begin.getTime();
//		long lendtime = _end.getTime();
//		
//		Map<String,Object> parameter = new HashMap<String,Object>();
//		parameter.put("username", username);
//		parameter.put("loginname", loginname);
//		parameter.put("enddate", lendtime);
//		parameter.put("begindate", lbegintime);
//		
//		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
//			page = Code.PAGEINFO_DEFAULT_PAGE;
//		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
//			count = Code.PAGEINFO_DEFAULT_COUNT;
//		
//		return parameter;
	}

}

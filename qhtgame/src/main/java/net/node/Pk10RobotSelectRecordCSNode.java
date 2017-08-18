package net.node;

public class Pk10RobotSelectRecordCSNode {

	private String period;
	private String nickname;
	private String begintime;
	private String endtime;
	private int page;
	private int count;
	
	
	public String getPeriod() {
		return period;
	}


	public void setPeriod(String period) {
		this.period = period;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getBegintime() {
		return begintime;
	}


	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}


	public String getEndtime() {
		return endtime;
	}


	public void setEndtime(String endtime) {
		this.endtime = endtime;
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

/*
	public Map<String,Object> getParameter() {
		if (	username==null || username.length()>31 ||
				nickname==null || nickname.length()>31 ||
				(status!=0 && status!=Code.STATUS_EFFECTIVE && status!=Code.STATUS_UNEFFECTIVE) ||
				begintime==null ||
				endtime==null)
			return null;
		Timestamp _begin = DateUtil.StringToTimestamp(begintime);
		Timestamp _end = DateUtil.StringToTimestamp(endtime);
		if (_begin==null || _end==null)
			return null;
		long lbegintime = _begin.getTime();
		long lendtime = _end.getTime();
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		parameter.put("nickname", nickname);
		parameter.put("status", status);
		parameter.put("begintime", lbegintime);
		parameter.put("endtime", lendtime);
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		return parameter;
	}*/
}

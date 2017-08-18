package qht.game.node;

public class AppinfoNode {

	private int id;
	private String appcode;
	private String agent;
	private String appname;
	private String appcompany;
	private String wechat_code;
	private String wechat_p;
	private String wechat_img;
	private long actiontime;
	private long createtime;
	private long updatetime;
	
	
	public String getWechat_img() {
		return wechat_img;
	}
	public void setWechat_img(String wechat_img) {
		this.wechat_img = wechat_img;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public long getActiontime() {
		return actiontime;
	}
	public void setActiontime(long actiontime) {
		this.actiontime = actiontime;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	@Override
	public String toString() {
		return "AppinfoNode  id=" + id + ", appcode=" + appcode + ", agent=" + agent + ", appname=" + appname
				+ ", appcompany=" + appcompany + ", wechat_code=" + wechat_code + ", wechat_p=" + wechat_p
				+ ", actiontime=" + actiontime + ", createtime=" + createtime + ", updatetime=" + updatetime + "";
	}

	

}

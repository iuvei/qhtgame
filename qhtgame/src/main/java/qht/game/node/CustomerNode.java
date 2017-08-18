package qht.game.node;

public class CustomerNode {

	private int id;
	private String cus_num;
	private String cus_pwd;
	private String cus_name;
	private long createtime;
	private long updatetime;
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
		return "CustomerNode id=" + id + ", cus_num=" + cus_num + ", cus_pwd=" + cus_pwd + ", cus_name=" + cus_name
				+ ", createtime=" + createtime + ", updatetime=" + updatetime + "";
	}

	

}

package qht.game.node;

import java.math.BigDecimal;
import java.util.List;

public class Pk10PlayerNode {
	private int id;
	private String appcode;
	private String username;
	private String nickname;
	private int status;			//1_有效  2_无效
	private BigDecimal balance;
	private List<RebotSchemeNode> schemes;
	private long createtime;
	private long updatetime;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public List<RebotSchemeNode> getSchemes() {
		return schemes;
	}
	public void setSchemes(List<RebotSchemeNode> schemes) {
		this.schemes = schemes;
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
}

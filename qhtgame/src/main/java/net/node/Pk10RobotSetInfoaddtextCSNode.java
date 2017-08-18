package net.node;

public class Pk10RobotSetInfoaddtextCSNode {
	private String username;	//玩家
	private int id;
	private int time;			//离开盘时间（秒）
	private String scheme;		//方案名
	private int probabi;		//概率
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public int getProbabi() {
		return probabi;
	}
	public void setProbabi(int probabi) {
		this.probabi = probabi;
	}
}

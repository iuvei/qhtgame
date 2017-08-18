package qht.game.node;

import java.math.BigDecimal;

public class Pk10RebotSchemeDB {
	private int id;
	private String name;
	private BigDecimal low_amount;			//剩余金额低于此值
	private BigDecimal sendup_amount;		//发送上分金额
	private BigDecimal up_amount;			//剩余金额高于此值
	private BigDecimal senddown_amount;		//发送下分金额
	private BigDecimal stop_amount;			//金额小于此值，停止投注
	private byte[] send_text;				//投注内容
	private long updatetime;
	private long createtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getLow_amount() {
		return low_amount;
	}
	public void setLow_amount(BigDecimal low_amount) {
		this.low_amount = low_amount;
	}
	public BigDecimal getSendup_amount() {
		return sendup_amount;
	}
	public void setSendup_amount(BigDecimal sendup_amount) {
		this.sendup_amount = sendup_amount;
	}
	public BigDecimal getUp_amount() {
		return up_amount;
	}
	public void setUp_amount(BigDecimal up_amount) {
		this.up_amount = up_amount;
	}
	public BigDecimal getSenddown_amount() {
		return senddown_amount;
	}
	public void setSenddown_amount(BigDecimal senddown_amount) {
		this.senddown_amount = senddown_amount;
	}
	public BigDecimal getStop_amount() {
		return stop_amount;
	}
	public void setStop_amount(BigDecimal stop_amount) {
		this.stop_amount = stop_amount;
	}
	public byte[] getSend_text() {
		return send_text;
	}
	public void setSend_text(byte[] send_text) {
		this.send_text = send_text;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
}

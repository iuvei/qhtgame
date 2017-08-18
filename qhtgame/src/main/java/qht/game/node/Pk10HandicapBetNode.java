package qht.game.node;

/*
-- context：投注内容		采用json格式存储
-- odd|runway|bettype|amount  runway:投注道1~11 11为冠亚和   bettype:投注内容  大小单双龙虎1~19中的一个  amount:压注金额
-- 示例
-- 	[
-- 		"8e486ef1b605497d9042bb72d6ff8961|1|大|10.00",
--		"8e486ef1b605497d9042bb72d6ff8962|2|小|10.00",
--		"8e486ef1b605497d9042bb72d6ff8963|3|单|10.00",
--		"8e486ef1b605497d9042bb72d6ff8964|4|双|10.00",
--		"8e486ef1b605497d9042bb72d6ff8965|5|龙|10.00",
--		"8e486ef1b605497d9042bb72d6ff8955|5|虎|10.00",
--		"8e486ef1b605497d9042bb72d6ff8966|6|3|10.00",
--		"8e486ef1b605497d9042bb72d6ff8911|11|单|10.00",
--		"8e486ef1b605497d9042bb72d6ff8921|11|15|10.00"
-- 	]
*/
public class Pk10HandicapBetNode {
	private long id;
	private String odd;	//单号
	private String period;	//期号
	private byte[] context;	//投注内容
	private byte[] eventuate;	//返回结果
	private int status;		//状态 1_投注 2_结算 3_处理失败
	private long updatetime;	//更新时间
	private long createtime;	//创建时间
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOdd() {
		return odd;
	}
	public void setOdd(String odd) {
		this.odd = odd;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public byte[] getContext() {
		return context;
	}
	public void setContext(byte[] context) {
		this.context = context;
	}
	public byte[] getEventuate() {
		return eventuate;
	}
	public void setEventuate(byte[] eventuate) {
		this.eventuate = eventuate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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

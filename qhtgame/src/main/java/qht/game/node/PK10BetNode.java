package qht.game.node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.util.DateUtil;

public class PK10BetNode {

	//<!--id big_small_low  big_small_high  single_double_low single_double_high  dragon_tiger_low dragon_tiger_high 
	//n_number_low n_number_high s_number_low s_number_high 
	//修改 下注			-->
	private int id;
	private BigDecimal big_small_low;// 大小最低
	private BigDecimal big_small_high;// 大小最高
	private BigDecimal single_double_low;// 单双最低
	private BigDecimal single_double_high;// 单双最高
	private BigDecimal dragon_tiger_low;//龙虎最低
	private BigDecimal dragon_tiger_high;// 龙虎最高
	private BigDecimal n_number_low;// 特码最低
	private BigDecimal n_number_high;// 特码最高
	private BigDecimal s_number_low;// 和值最低
	private BigDecimal s_number_high;// 和值最高
	private BigDecimal allBet;// 和值最高
	
	private long createtime;
	private long updatetime;
	
	
		public BigDecimal getAllBet() {
		return allBet;
	}


	public void setAllBet(BigDecimal allBet) {
		this.allBet = allBet;
	}


		public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public BigDecimal getBig_small_low() {
		return big_small_low;
	}


	public void setBig_small_low(BigDecimal big_small_low) {
		this.big_small_low = big_small_low;
	}


	public BigDecimal getBig_small_high() {
		return big_small_high;
	}


	public void setBig_small_high(BigDecimal big_small_high) {
		this.big_small_high = big_small_high;
	}


	public BigDecimal getSingle_double_low() {
		return single_double_low;
	}


	public void setSingle_double_low(BigDecimal single_double_low) {
		this.single_double_low = single_double_low;
	}


	public BigDecimal getSingle_double_high() {
		return single_double_high;
	}


	public void setSingle_double_high(BigDecimal single_double_high) {
		this.single_double_high = single_double_high;
	}


	public BigDecimal getDragon_tiger_low() {
		return dragon_tiger_low;
	}


	public void setDragon_tiger_low(BigDecimal dragon_tiger_low) {
		this.dragon_tiger_low = dragon_tiger_low;
	}


	public BigDecimal getDragon_tiger_high() {
		return dragon_tiger_high;
	}


	public void setDragon_tiger_high(BigDecimal dragon_tiger_high) {
		this.dragon_tiger_high = dragon_tiger_high;
	}


	public BigDecimal getN_number_low() {
		return n_number_low;
	}


	public void setN_number_low(BigDecimal n_number_low) {
		this.n_number_low = n_number_low;
	}


	public BigDecimal getN_number_high() {
		return n_number_high;
	}


	public void setN_number_high(BigDecimal n_number_high) {
		this.n_number_high = n_number_high;
	}


	public BigDecimal getS_number_low() {
		return s_number_low;
	}


	public void setS_number_low(BigDecimal s_number_low) {
		this.s_number_low = s_number_low;
	}


	public BigDecimal getS_number_high() {
		return s_number_high;
	}


	public void setS_number_high(BigDecimal s_number_high) {
		this.s_number_high = s_number_high;
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
	//<!--id big_small_low  big_small_high  single_double_low single_double_high  dragon_tiger_low dragon_tiger_high 
		//n_number_low n_number_high s_number_low s_number_high 
	public Map<String, Object> getParameter() {
		if (id <= 0)
			return null;
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("id", id);
		parameter.put("big_small_low", big_small_low);
		parameter.put("big_small_high", big_small_high);
		parameter.put("single_double_low", single_double_low);
		parameter.put("single_double_high", single_double_high);
		parameter.put("dragon_tiger_low", dragon_tiger_low);
		parameter.put("dragon_tiger_high", dragon_tiger_high);
		parameter.put("n_number_low", n_number_low);
		parameter.put("n_number_high", n_number_high);
		parameter.put("s_number_low", s_number_low);
		parameter.put("s_number_high", s_number_high);
		parameter.put("allBet", allBet);
		parameter.put("createtime", DateUtil.getCurTimestamp().getTime());
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}

}

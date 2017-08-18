package qht.game.node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.util.CoinUtil;
import com.util.DateUtil;

public class PK10PaidNode {
	// <!--id n_number n_big n_small n_single n_double n_dragon n_tiger s_big
	// s_small s_single s_double
	// s_number_341819 s_number_561617 s_number_781415 s_number_9101213
	// s_number_11
	// -->
	private int id;
	private BigDecimal n_number;// 特码数字赔付
	private BigDecimal n_big;// 1~10大赔付
	private BigDecimal n_small;// 1~10小赔付
	private BigDecimal n_single;// 1~10单赔付
	private BigDecimal n_double;//1~10 双赔付
	private BigDecimal n_dragon;// 龙赔付
	private BigDecimal n_tiger;// 虎赔付
	private BigDecimal s_big;// 和大赔付
	private BigDecimal s_small;// 和小赔付
	private BigDecimal s_single;// 和单赔付
	private BigDecimal s_double;// 和双赔付
	private BigDecimal s_number_341819;// 冠亚和数字341819赔付
	private BigDecimal s_number_561617;// 冠亚和数字561617赔付
	private BigDecimal s_number_781415;// 冠亚和数字781415赔付
	private BigDecimal s_number_9101213;// 冠亚和数字9101213赔付
	private BigDecimal s_number_11;// 冠亚和数字11赔付
	private long createtime;
	private long updatetime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getN_number() {
		return n_number;
	}

	public void setN_number(BigDecimal n_number) {
		this.n_number = n_number;
	}

	public BigDecimal getN_big() {
		return n_big;
	}

	public void setN_big(BigDecimal n_big) {
		this.n_big = n_big;
	}

	public BigDecimal getN_small() {
		return n_small;
	}

	public void setN_small(BigDecimal n_small) {
		this.n_small = n_small;
	}

	public BigDecimal getN_single() {
		return n_single;
	}

	public void setN_single(BigDecimal n_single) {
		this.n_single = n_single;
	}

	public BigDecimal getN_double() {
		return n_double;
	}

	public void setN_double(BigDecimal n_double) {
		this.n_double = n_double;
	}

	public BigDecimal getN_dragon() {
		return n_dragon;
	}

	public void setN_dragon(BigDecimal n_dragon) {
		this.n_dragon = n_dragon;
	}

	public BigDecimal getN_tiger() {
		return n_tiger;
	}

	public void setN_tiger(BigDecimal n_tiger) {
		this.n_tiger = n_tiger;
	}

	public BigDecimal getS_big() {
		return s_big;
	}

	public void setS_big(BigDecimal s_big) {
		this.s_big = s_big;
	}

	public BigDecimal getS_small() {
		return s_small;
	}

	public void setS_small(BigDecimal s_small) {
		this.s_small = s_small;
	}

	public BigDecimal getS_single() {
		return s_single;
	}

	public void setS_single(BigDecimal s_single) {
		this.s_single = s_single;
	}

	public BigDecimal getS_double() {
		return s_double;
	}

	public void setS_double(BigDecimal s_double) {
		this.s_double = s_double;
	}

	public BigDecimal getS_number_341819() {
		return s_number_341819;
	}

	public void setS_number_341819(BigDecimal s_number_341819) {
		this.s_number_341819 = s_number_341819;
	}

	public BigDecimal getS_number_561617() {
		return s_number_561617;
	}

	public void setS_number_561617(BigDecimal s_number_561617) {
		this.s_number_561617 = s_number_561617;
	}

	public BigDecimal getS_number_781415() {
		return s_number_781415;
	}

	public void setS_number_781415(BigDecimal s_number_781415) {
		this.s_number_781415 = s_number_781415;
	}

	public BigDecimal getS_number_9101213() {
		return s_number_9101213;
	}

	public void setS_number_9101213(BigDecimal s_number_9101213) {
		this.s_number_9101213 = s_number_9101213;
	}

	public BigDecimal getS_number_11() {
		return s_number_11;
	}

	public void setS_number_11(BigDecimal s_number_11) {
		this.s_number_11 = s_number_11;
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

	public Map<String, Object> getParameter() {
		if (id <= 0)
			return null;
		// <!--id n_number n_big n_small n_single n_double n_dragon n_tiger s_big
		// s_small s_single s_double
		// s_number_341819 s_number_561617 s_number_781415 s_number_9101213
		// s_number_11
		// -->
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("id", id);
		parameter.put("n_number", n_number);
		parameter.put("n_big", n_big);
		parameter.put("n_small", n_small);
		parameter.put("n_single", n_single);
		parameter.put("n_double", n_double);
		parameter.put("n_dragon", n_dragon);
		parameter.put("n_tiger", n_tiger);
		parameter.put("s_big", s_big);
		parameter.put("s_small", s_small);
		parameter.put("s_single", s_single);
		parameter.put("s_double", s_double);
		parameter.put("s_number_341819", s_number_341819);
		parameter.put("s_number_561617", s_number_561617);
		parameter.put("s_number_781415", s_number_781415);
		parameter.put("s_number_9101213", s_number_9101213);
		parameter.put("s_number_11", s_number_11);
		parameter.put("createtime", DateUtil.getCurTimestamp().getTime());
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		return parameter;
	}

	public BigDecimal getS_number(int hit) {
		BigDecimal paid = CoinUtil.zero;
		switch (hit) {
		case 3:
		case 4:
		case 18:
		case 19:
			paid = s_number_341819;
			break;
		case 5:
		case 6:
		case 16:
		case 17:
			paid = s_number_561617;
			break;
		case 7:
		case 8:
		case 14:
		case 15:
			paid = s_number_781415;
			break;
		case 9:
		case 10:
		case 12:
		case 13:
			paid = s_number_9101213;
			break;
		case 11:
			paid = s_number_11;
			break;
		default:
			paid = CoinUtil.zero;
			break;
		}
		return paid;
	}

}

package com.pk10.node;

import java.math.BigDecimal;
import java.util.List;

import com.node.ComIntBigNode;

public class Pk10BetInfo {
	private int index;
	private List<ComIntBigNode> num;
	private BigDecimal big;
	private BigDecimal small;
	private BigDecimal single;
	private BigDecimal dou;
	private BigDecimal dragon;
	private BigDecimal tiger;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public List<ComIntBigNode> getNum() {
		return num;
	}
	public void setNum(List<ComIntBigNode> num) {
		this.num = num;
	}
	public BigDecimal getBig() {
		return big;
	}
	public void setBig(BigDecimal big) {
		this.big = big;
	}
	public BigDecimal getSmall() {
		return small;
	}
	public void setSmall(BigDecimal small) {
		this.small = small;
	}
	public BigDecimal getSingle() {
		return single;
	}
	public void setSingle(BigDecimal single) {
		this.single = single;
	}
	public BigDecimal getDou() {
		return dou;
	}
	public void setDou(BigDecimal dou) {
		this.dou = dou;
	}
	public BigDecimal getDragon() {
		return dragon;
	}
	public void setDragon(BigDecimal dragon) {
		this.dragon = dragon;
	}
	public BigDecimal getTiger() {
		return tiger;
	}
	public void setTiger(BigDecimal tiger) {
		this.tiger = tiger;
	}
}

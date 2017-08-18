package com.node;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.util.CoinUtil;

public class Pk10Info {

	private int runway;
	private BigDecimal big;
	private BigDecimal small;
	private BigDecimal single;
	private BigDecimal dou;
	private BigDecimal dragon;
	private BigDecimal tiger;
	private List<Pk10Node> arrNum;
	
	private List<String> arrOdd;
	
	private BigDecimal allAmount;
	public Pk10Info(int runway) {
		super();
		this.runway = runway;
		this.big = CoinUtil.zero;
		this.small = CoinUtil.zero;
		this.single = CoinUtil.zero;
		this.dou = CoinUtil.zero;
		this.dragon = CoinUtil.zero;
		this.tiger = CoinUtil.zero;
		this.arrNum = new ArrayList<Pk10Node>();
		this.allAmount = CoinUtil.zero;
		this.arrOdd = new ArrayList<String>();
	}
	public int getRunway() {
		return runway;
	}
	public BigDecimal getBig() {
		return big;
	}
	public void addBig(BigDecimal big) {
		this.big = CoinUtil.add(this.big,big);
		this.allAmount = CoinUtil.add(this.allAmount, big);
	}
	public BigDecimal getSmall() {
		return small;
	}
	public void addSmall(BigDecimal small) {
		this.small = CoinUtil.add(this.small,small);
		this.allAmount = CoinUtil.add(this.allAmount, small);
	}
	public BigDecimal getSingle() {
		return single;
	}
	public void addSingle(BigDecimal single) {
		this.single = CoinUtil.add(this.single,single);
		this.allAmount = CoinUtil.add(this.allAmount, single);
	}
	public BigDecimal getDou() {
		return dou;
	}
	public void addDou(BigDecimal dou) {
		this.dou = CoinUtil.add(this.dou , dou);
		this.allAmount = CoinUtil.add(this.allAmount, dou);
	}
	public BigDecimal getDragon() {
		return dragon;
	}
	public void addDragon(BigDecimal dragon) {
		this.dragon = CoinUtil.add(this.dragon,dragon);
		this.allAmount = CoinUtil.add(this.allAmount, dragon);
	}
	public BigDecimal getTiger() {
		return tiger;
	}
	public void addTiger(BigDecimal tiger) {
		this.tiger = CoinUtil.add(this.tiger , tiger);
		this.allAmount = CoinUtil.add(this.allAmount, tiger);
	}
	public List<Pk10Node> getArrNum() {
		return arrNum;
	}
	public BigDecimal getAllAmount() {
		return allAmount;
	}
	public void addArrNum(int index,BigDecimal amount) {
		Pk10Node pk10 = null;
		for (Pk10Node node : arrNum) {
			if (node.getIndex()==index) {
				pk10 = node;
				break;
			}
		}
		if (pk10==null) {
			pk10 = new Pk10Node(index,amount);
		} else {
			arrNum.remove(pk10);
			BigDecimal _amount = pk10.getAmount();
			_amount = CoinUtil.add(_amount, amount);
			pk10 = new Pk10Node(index,_amount);
		}
		this.allAmount = CoinUtil.add(this.allAmount, amount);
		arrNum.add(pk10);
	}
	public BigDecimal getNum(int index) {
		Pk10Node pk10 = null;
		for (Pk10Node node : arrNum) {
			if (node.getIndex()==index) {
				pk10 = node;
				break;
			}
		}
		if (pk10==null)
			return CoinUtil.zero;
		return pk10.getAmount();
	}
	public List<String> getArrOdd() {
		return arrOdd;
	}
	public void addArrOdd(String odd) {
		this.arrOdd.add(odd);
	}
}

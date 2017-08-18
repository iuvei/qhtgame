package com.pk10.node;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.node.ComIntBigNode;
import com.util.CoinUtil;
import com.util.ComUtil;

public class Pk10Bet {
	private String id;
	private int status;	//1:压注  2：取消
	private int index;
	private List<ComIntBigNode> num;
	private BigDecimal big;
	private BigDecimal small;
	private BigDecimal single;
	private BigDecimal dou;
	private BigDecimal dragon;
	private BigDecimal tiger;
	
	public Pk10Bet() {
		super();
		this.id = ComUtil.get32UUID();
		this.status = 0;
		this.index = 0;
		this.num = new ArrayList<ComIntBigNode>();
		this.big = CoinUtil.zero;
		this.small = CoinUtil.zero;
		this.single = CoinUtil.zero;
		this.dou = CoinUtil.zero;
		this.dragon = CoinUtil.zero;
		this.tiger = CoinUtil.zero;
	}

	public void addNum(int number, BigDecimal money) {
		if (number>0 && number<=19 && money!=null && CoinUtil.compareZero(money)>0) {
			boolean isExist = false;
			for (ComIntBigNode node : num) {
				if (node.getId()==number) {
					node.setMoney(CoinUtil.add(node.getMoney(),money));
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				ComIntBigNode node = new ComIntBigNode();
				node.setId(number);
				node.setMoney(money);
				num.add(node);
			}
		}
	}

	public void addBig(BigDecimal big) {
		this.big = CoinUtil.add(this.big, big);
	}

	public void addSmall(BigDecimal small) {
		this.small = CoinUtil.add(this.small, small);
	}

	public void addSingle(BigDecimal single) {
		this.single = CoinUtil.add(this.single, single);
	}

	public void addDou(BigDecimal dou) {
		this.dou = CoinUtil.add(this.dou, dou);
	}

	public void addDragon(BigDecimal dragon) {
		this.dragon = CoinUtil.add(this.dragon, dragon);
	}

	public void addTiger(BigDecimal tiger) {
		this.tiger = CoinUtil.add(this.tiger, tiger);
	}


	public BigDecimal getBet() {
		BigDecimal bet = CoinUtil.zero;
		bet = CoinUtil.add(bet,this.big);
		bet = CoinUtil.add(bet,this.small);
		bet = CoinUtil.add(bet,this.single);
		bet = CoinUtil.add(bet,this.dou);
		bet = CoinUtil.add(bet,this.dragon);
		bet = CoinUtil.add(bet,this.tiger);
		for (ComIntBigNode node : num) {
			bet = CoinUtil.add(bet, node.getMoney());
		}
		return bet;
	}
	@Override
	public String toString() {
		String result = null;
		if (index==1)
			result = "冠军 ";
		else if (index==2)
			result = "亚军 ";
		else if (index==11)
			result = "冠亚和 ";
		else
			result = "第"+index+"名 ";
		if (CoinUtil.compareZero(big)>0)
			result += "大" + CoinUtil.Round(big) + " ";
		if (CoinUtil.compareZero(small)>0)
			result += "小" + CoinUtil.Round(small) + " ";
		if (CoinUtil.compareZero(single)>0)
			result += "单" + CoinUtil.Round(single) + " ";
		if (CoinUtil.compareZero(dou)>0)
			result += "双" + CoinUtil.Round(dou) + " ";
		if (CoinUtil.compareZero(dragon)>0)
			result += "龙" + CoinUtil.Round(dragon) + " ";
		if (CoinUtil.compareZero(tiger)>0)
			result += "虎" + CoinUtil.Round(tiger) + " ";
		if (num.size()>0) {
			result += "数字[";
			for (ComIntBigNode node : num) {
				result += node.getId() + "_" + CoinUtil.Round(node.getMoney()) +" ";
			}
			result = result.trim();
			result += "]";
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

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
	
	public String myBetInfo() {
		String result = "";
		if (index>1 && index<=10) {
			if (index==10)
				result += "0 ";
			else
				result += index + " ";
		} else if (index==11) {
			result += "特 ";
		}
		
		BigDecimal money = CoinUtil.zero;
		if (CoinUtil.compareZero(big)>0) {
			result += "大";
			money = big;
		}
		if (CoinUtil.compareZero(small)>0) {
			result += "小";
			money = small;
		}
		if (CoinUtil.compareZero(single)>0) {
			result += "单";
			money = single;
		}
		if (CoinUtil.compareZero(dou)>0) {
			result += "双";
			money = dou;
		}
		if (CoinUtil.compareZero(dragon)>0) {
			result += "龙";
			money = dragon;
		}
		if (CoinUtil.compareZero(tiger)>0) {
			result += "虎";
			money = tiger;
		}
		for (ComIntBigNode node : num) {
			if (CoinUtil.compareZero(node.getMoney())>0) {
				int id = node.getId();
				if (index<=10) {
					if (id==10)
						result += "0";
					else
						result += id;
				} else {
					result += id;
				}
				
				money = node.getMoney();
			}
		}
		result += " " + money;
		
		return result;
	}
}

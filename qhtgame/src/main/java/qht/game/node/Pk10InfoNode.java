package qht.game.node;

import java.math.BigDecimal;
import java.util.List;

import com.sysconst.Code;
import com.util.CoinUtil;
import com.util.DateUtil;

public class Pk10InfoNode {
	private long id;				//自增ID
	private String odd;				//单号
	private String period;			//期号
	private String appcode;			
	private String username;		//玩家
	private int runway;				//车道(1~10表示冠军道~第10道 11表示冠亚和)
	private int bettype;			//投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	private BigDecimal betamount;	//投注金额
	private BigDecimal paidamount;	//投注金额
	private long bettime;			//投注时间
	private int status;				//状态		(1:投注 2：结算 3：取消 4:飞盘)
	private long paidtime;			//赔付时间
	private long updatetime;		//更新时间
	public Pk10InfoNode() {
		super();
	}
	
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

	public int getRunway() {
		return runway;
	}

	public void setRunway(int runway) {
		this.runway = runway;
	}

	public int getBettype() {
		return bettype;
	}

	public void setBettype(int bettype) {
		this.bettype = bettype;
	}

	public BigDecimal getBetamount() {
		return betamount;
	}

	public void setBetamount(BigDecimal betamount) {
		this.betamount = betamount;
	}

	public BigDecimal getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}

	public long getBettime() {
		return bettime;
	}

	public void setBettime(long bettime) {
		this.bettime = bettime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getPaidtime() {
		return paidtime;
	}

	public void setPaidtime(long paidtime) {
		this.paidtime = paidtime;
	}

	public long getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	
	public boolean isBet() {
		if (status==Code.PK10_BET || status==Code.PK10_HANDICAP)
			return true;
		return false;
	}

	public boolean settle(List<Integer> listPrize, PK10PaidNode iniPaid) {
		boolean result = false;
		if (runway<11) {
			int hit = listPrize.get(runway-1);
			int _hit = listPrize.get(10-runway);
			boolean isBig = true;
			boolean isSingle = true;
			boolean isDragon = true;
			if (hit<=5)
				isBig = false;
			if (hit%2==0)
				isSingle = false;
			if (hit<_hit)
				isDragon = false;
			
			if (bettype == hit) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getN_number());
				result = true;
			} else if (isBig && bettype == Code.PK10_BIG) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getN_big());
				result = true;
			} else if (!isBig && bettype == Code.PK10_SMALL) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getN_small());
				result = true;
			} else if (isSingle && bettype == Code.PK10_SINGLE) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getN_single());
				result = true;
			} else if (!isSingle && bettype == Code.PK10_DOU) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getN_double());
				result = true;
			} else if (isDragon && (runway>=1&&runway<=5) && bettype == Code.PK10_DRAGON) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getN_dragon());
				result = true;
			} else if (!isDragon && (runway>=1&&runway<=5) && bettype == Code.PK10_TIGER) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getN_tiger());
				result = true;
			}
		} else {
			int hit = listPrize.get(0) + listPrize.get(1);
			boolean isBig = true;
			boolean isSingle = true;
			if (hit<=11)
				isBig = false;
			if (hit%2==0)
				isSingle = false;
			
			if (bettype == hit) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getS_number(hit));
				result = true;
			} else if (isBig && bettype == Code.PK10_BIG) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getS_big());
				result = true;
			} else if (!isBig && bettype == Code.PK10_SMALL) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getS_small());
				result = true;
			} else if (isSingle && bettype == Code.PK10_SINGLE) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getS_single());
				result = true;
			} else if (!isSingle && bettype == Code.PK10_DOU) {
				paidamount = CoinUtil.mul(betamount, iniPaid.getS_double());
				result = true;
			}
		}
		
		long tmp = DateUtil.getCurTimestamp().getTime();
		status = Code.PK10_SETTLE;
		paidtime = tmp;
		updatetime = tmp;
		
		return result;
	}

	public String _getBetInfo() {
		String strRunway = _getRunway(runway);
		String strBettype = _getBettype(bettype);
		return strRunway+" "+strBettype+"-"+CoinUtil.Round(betamount);
	}
	private static String _getRunway(int runway) {
		String result = null;
		switch (runway) {
		case 1:
			result = "第一名";
			break;
		case 2:
			result = "第二名";
			break;
		case 3:
			result = "第三名";
			break;
		case 4:
			result = "第四名";
			break;
		case 5:
			result = "第五名";
			break;
		case 6:
			result = "第六名";
			break;
		case 7:
			result = "第七名";
			break;
		case 8:
			result = "第八名";
			break;
		case 9:
			result = "第九名";
			break;
		case 10:
			result = "第十名";
			break;
		case 11:
			result = "冠亚";
			break;
		}
		return result;
	}

	private static String _getBettype(int bettype) {
		if (bettype==Code.PK10_BIG) {
			return "大";
		} else if (bettype==Code.PK10_SMALL) {
			return "小";
		} else if (bettype==Code.PK10_SINGLE){
			return "单";
		} else if (bettype==Code.PK10_DOU) {
			return "双";
		} else if (bettype==Code.PK10_DRAGON) {
			return "龙";
		} else if (bettype==Code.PK10_TIGER) {
			return "虎";
		} else  {
			return String.valueOf(bettype);
		}
	}
	
	public String _getBetInfoR() {
		String strRunway = _getRunwayR(runway);
		String strBettype = _getBettypeR(runway,bettype);
		return strRunway+" "+strBettype+" "+CoinUtil.Round(betamount);
	}
	private static String _getRunwayR(int runway) {
		if (runway==11)
			return "特";
		else if (runway==10)
			return "0";
		else
			return String.valueOf(runway);
	}

	private static String _getBettypeR(int runway, int bettype) {
		if (bettype==Code.PK10_BIG) {
			return "大";
		} else if (bettype==Code.PK10_SMALL) {
			return "小";
		} else if (bettype==Code.PK10_SINGLE){
			return "单";
		} else if (bettype==Code.PK10_DOU) {
			return "双";
		} else if (bettype==Code.PK10_DRAGON) {
			return "龙";
		} else if (bettype==Code.PK10_TIGER) {
			return "虎";
		} else  {
			if (runway<11) {
				if (bettype==10)
					return "0";
				else
					return String.valueOf(bettype);
			} else {
				return String.valueOf(bettype); 
			}
		}
	}
	
}


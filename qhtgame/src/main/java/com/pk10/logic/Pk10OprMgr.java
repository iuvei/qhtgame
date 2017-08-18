/*
 * 玩家上、下分，投注，撤注操作类
 */
package com.pk10.logic;

import java.math.BigDecimal;
import java.util.List;

import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.node.ComIntBigNode;
import com.node.ErrorCode;
import com.node.UserInfo;
import com.pk10.node.Pk10BetTextNode;
import com.pk10.node.Pk10OpenInfo;
import com.sysconst.Code;
import com.util.CoinUtil;
import com.util.ComUtil;
import com.util.DateUtil;

import qht.game.dao.PlayerDao;
import qht.game.node.FinanceOrderNode;
import qht.game.node.PK10BetNode;
import qht.game.node.Pk10InfoNode;
import qht.game.node.Pk10PlayerNode;
import qht.game.node.PlayerNode;

public class Pk10OprMgr {

	/*
	 * 预处理（上下分、投注），用于格式判断
	 * @param	
	 * 		fromID:请求者
	 * 		sendID:发送者
	 * 		groupID:群组ID
	 * 		text:内容
	 */
	public static ErrorCode predeal(String fromID, String sendID,String groupID,String text) {
		if (text==null || text.length()==0)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		text = Pk10TextMgr.preDeal(text);
		if (text==null || text.length()==0)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		
		ErrorCode result = null;
		if (Pk10TextMgr.isUpDown(text))
			result = preDealUpDown(fromID,sendID, groupID, text);
		else
			result = preDealBet(fromID,sendID, groupID, text);
		return result;
	}
	
	/*
	 * 处理（上下分、投注）
	 * @param	
	 * 		fromID:请求者
	 * 		sendID:发送者
	 * 		groupID:群组ID
	 * 		text:内容
	 */
	public static ErrorCode deal(String fromID, String sendID,String groupID,String text) {
		if (text==null || text.length()==0)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		text = Pk10TextMgr.preDeal(text);
		if (text==null || text.length()==0)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		
		ErrorCode result = null;
		if (Pk10TextMgr.isUpDown(text))
			result = dealUpDown(fromID,sendID, groupID, text);
		else
			result = dealBet(fromID,sendID, groupID, text);
		return result;
	}
	
	/*
	 * 处理上下分
	 * 	 	fromID:请求者
	 * 		sendID:发送者
	 * 		groupID:群组ID
	 * 		text:内容
	 */
	private static ErrorCode dealUpDown(String fromID, String sendID, String groupID, String text) {
		UserInfo userInfo = PlayerMgr.getByName(fromID);
		if (userInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】不在线");
		PlayerNode player = userInfo.getPlayer();
		String nickname = player.getNickname();
		
		ComIntBigNode node = Pk10TextMgr.parseUpDown(text);
		if (node==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】格式不对 \n【上/下10】");
		
		String username = fromID;
		int type = node.getId();
		BigDecimal amount = node.getMoney();
		if ( 	(type!=Code.AMOUNT_RECHANGE && type!=Code.AMOUNT_WITHDRAWALS) ||
				(amount==null || CoinUtil.compareZero(amount)<=0 ) )
		return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】格式不对");
		
		
		BigDecimal balance = player.getBalance();
		if (type==Code.AMOUNT_WITHDRAWALS) {
			if (CoinUtil.compare(balance, amount)<0)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】余额不足");
			balance = CoinUtil.sub(balance, amount);
		} else {
			balance = CoinUtil.add(balance, amount);
		}
		
		FinanceOrderNode record = new FinanceOrderNode();
		record.setOdd(ComUtil.get32UUID());
		record.setUsername(username);
		record.setType(type);
		record.setAmount(amount);
		record.setRequesttime(DateUtil.getCurTimestamp().getTime());
		record.setRequestname(username);
		record.setTag(Code.TAG_NODEAL);
		record.setUpdatetime(DateUtil.getCurTimestamp().getTime());
		if (!DaoMgr.getFinanceOrder().insert(record))
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】请求失败");
		
		return new ErrorCode(Code.OK_CODE,"玩家【"+nickname+"】请求【"+text+"】成功,等待后台处理");
	}

	/*
	 * 处理投注
	 * 	 	fromID:请求者
	 * 		sendID:发送者
	 * 		groupID:群组ID
	 * 		text:内容
	 */
	private static ErrorCode dealBet(String fromID, String sendID,String groupID,String text) {
		if (Pk10PrizeMgr.isSealPlate())
			return new ErrorCode(Code.PK10_BET_FAIL,"已封盘");
		Pk10OpenInfo openInfo = Pk10PrizeMgr.getPk10OpenInfo();
		if (openInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"游戏未开始");
		long tmpTime = openInfo.getNextOpentime() - openInfo.getTimestamp();
		if (tmpTime>300000)	//如果下一局相差超过5分钟，不让玩家投注（处理夜间不开奖玩家还能投注的问题）
			return new ErrorCode(Code.PK10_BET_FAIL,"游戏未开始");
		
		
		UserInfo userInfo = PlayerMgr.getByName(fromID);
		if (userInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】不在线");
		PlayerNode player = userInfo.getPlayer();
		
		Pk10BetTextNode betInfo = Pk10TextMgr.parseBet(text);
		if (betInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		Pk10OpenInfo pk10OpenInfo = Pk10PrizeMgr.getPk10OpenInfo();
		if (pk10OpenInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		String period = pk10OpenInfo.getNextPeriod();
		List<Pk10InfoNode> arrBet = Pk10TextMgr.get(period, betInfo, player.getAppcode(), player.getUsername());
		if (arrBet==null || arrBet.size()==0)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		
		//下注限制
		BigDecimal allBet = Pk10PrizeMgr.getPk10Player().getRealAllBet();//当期下注所有金额总和
		if (!betLowHigh(arrBet,allBet))
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】输入金额范围有误");
		
		BigDecimal bet = CoinUtil.zero;
		for (Pk10InfoNode node : arrBet) {
			bet = CoinUtil.add(bet,node.getBetamount());
		}
		
		if (CoinUtil.compare(player.getBalance(),bet)<0)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+player.getUsername()+"】投注失败 原因【余额不足】  余额【"+CoinUtil.Round(player.getBalance())+"】");
		
		
		//获取压注信息
		Pk10Player pk10Player = Pk10PrizeMgr.getPk10Player();
		for (Pk10InfoNode node : arrBet) {
			if (!pk10Player.bet(node,player))
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】投注失败");
		}

		String strRel = "玩家【"+player.getNickname()+"】投注成功\n";
		for (Pk10InfoNode node : arrBet)
			strRel += node._getBetInfo() + "\n";
		strRel += "当期投注额【"+CoinUtil.Round(bet)+"】";
		
		return new ErrorCode(Code.OK_CODE,strRel);
	}
	
	/*
	 * 上下分格式判断
	 * 	 	fromID:请求者
	 * 		sendID:发送者
	 * 		groupID:群组ID
	 * 		text:内容
	 */
	private static ErrorCode preDealUpDown(String fromID, String sendID, String groupID, String text) {
		
		ComIntBigNode node = Pk10TextMgr.parseUpDown(text);
		if (node==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对 \n【上/下10】");
		
		int type = node.getId();
		BigDecimal amount = node.getMoney();
		if ( 	(type!=Code.AMOUNT_RECHANGE && type!=Code.AMOUNT_WITHDRAWALS) ||
				(amount==null || CoinUtil.compareZero(amount)<=0 ) )
		return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		
		UserInfo userInfo = PlayerMgr.getByName(fromID);
		if (userInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】不在线");
		PlayerNode player = userInfo.getPlayer();
		BigDecimal balance = player.getBalance();
		if (type==Code.AMOUNT_WITHDRAWALS) {
			if (CoinUtil.compare(balance, amount)<0)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】余额不足");
			balance = CoinUtil.sub(balance, amount);
		}
		return new ErrorCode(Code.OK_CODE,Code.OK_DESC);
	}
	
	/*
	 * 投注格式判断
	 * 	 	fromID:请求者
	 * 		sendID:发送者
	 * 		groupID:群组ID
	 * 		text:内容
	 */
	private static ErrorCode preDealBet(String fromID, String sendID,String groupID,String text) {
		if (Pk10PrizeMgr.isSealPlate())
			return new ErrorCode(Code.PK10_BET_FAIL,"已封盘");
		Pk10OpenInfo openInfo = Pk10PrizeMgr.getPk10OpenInfo();
		if (openInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"游戏未开始");
		long tmpTime = openInfo.getNextOpentime() - openInfo.getTimestamp();
		if (tmpTime>300000)	//如果下一局相差超过5分钟，不让玩家投注（处理夜间不开奖玩家还能投注的问题）
			return new ErrorCode(Code.PK10_BET_FAIL,"游戏未开始");
		
		
		UserInfo userInfo = PlayerMgr.getByName(fromID);
		if (userInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】不在线");
		PlayerNode player = userInfo.getPlayer();
		
		Pk10BetTextNode betInfo = Pk10TextMgr.parseBet(text);
		if (betInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		Pk10OpenInfo pk10OpenInfo = Pk10PrizeMgr.getPk10OpenInfo();
		if (pk10OpenInfo==null)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		String period = pk10OpenInfo.getNextPeriod();
		List<Pk10InfoNode> arrBet = Pk10TextMgr.get(period, betInfo, player.getAppcode(), player.getUsername());
		if (arrBet==null || arrBet.size()==0)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
		
		//下注限制
		BigDecimal allBet = Pk10PrizeMgr.getPk10Player().getRealAllBet();
		if (!betLowHigh(arrBet,allBet))
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】输入金额范围有误");
		
		BigDecimal bet = CoinUtil.zero;
		for (Pk10InfoNode node : arrBet) {
			bet = CoinUtil.add(bet,node.getBetamount());
		}
		
		if (CoinUtil.compare(player.getBalance(),bet)<0)
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+player.getUsername()+"】投注失败 原因【余额不足】  余额【"+CoinUtil.Round(player.getBalance())+"】");
		
		
		return new ErrorCode(Code.OK_CODE,Code.OK_DESC);
	}

	/*
	 * 下注限制
	 */
	private static boolean betLowHigh (List<Pk10InfoNode> arrBet,BigDecimal allBet){
		BigDecimal tmpAllBet = CoinUtil.zero;
		
		PK10BetNode ini = Pk10PrizeMgr.getMpaid(Code.LORREY_PK10);
		for (Pk10InfoNode node : arrBet) {
			int runway = node.getRunway();
			int bettype = node.getBettype();
			BigDecimal betamount = node.getBetamount();
			tmpAllBet = CoinUtil.add(tmpAllBet, betamount);
			if (runway>=1 && runway<=5) {
				if (bettype==Code.PK10_BIG || bettype==Code.PK10_SMALL) {
					if (CoinUtil.compare(betamount, ini.getBig_small_low())<0 || CoinUtil.compare(betamount, ini.getBig_small_high())>0)
						return false;
				} else if (bettype==Code.PK10_SINGLE || bettype==Code.PK10_DOU) {
					if (CoinUtil.compare(betamount, ini.getSingle_double_low())<0 || CoinUtil.compare(betamount, ini.getSingle_double_high())>0)
						return false;
				} else if (bettype==Code.PK10_DRAGON || bettype==Code.PK10_TIGER) {
					if (CoinUtil.compare(betamount, ini.getDragon_tiger_low())<0 || CoinUtil.compare(betamount, ini.getDragon_tiger_high())>0)
						return false;
				} else {
					if (CoinUtil.compare(betamount, ini.getN_number_low())<0 || CoinUtil.compare(betamount, ini.getN_number_high())>0)
						return false;
				}
			} else if (runway>=6 && runway<=10) {
				if (bettype==Code.PK10_BIG || bettype==Code.PK10_SMALL) {
					if (CoinUtil.compare(betamount, ini.getBig_small_low())<0 || CoinUtil.compare(betamount, ini.getBig_small_high())>0)
						return false;
				} else if (bettype==Code.PK10_SINGLE || bettype==Code.PK10_DOU) {
					if (CoinUtil.compare(betamount, ini.getSingle_double_low())<0 || CoinUtil.compare(betamount, ini.getSingle_double_high())>0)
						return false;
				} else {
					if (CoinUtil.compare(betamount, ini.getN_number_low())<0 || CoinUtil.compare(betamount, ini.getN_number_high())>0)
						return false;
				}
			} else if (runway==11) {
				if (bettype==Code.PK10_BIG || bettype==Code.PK10_SMALL) {
					if (CoinUtil.compare(betamount, ini.getBig_small_low())<0 || CoinUtil.compare(betamount, ini.getBig_small_high())>0)
						return false;
				} else if (bettype==Code.PK10_SINGLE || bettype==Code.PK10_DOU) {
					if (CoinUtil.compare(betamount, ini.getSingle_double_low())<0 || CoinUtil.compare(betamount, ini.getSingle_double_high())>0)
						return false;
				} else {
					if (CoinUtil.compare(betamount, ini.getS_number_low())<0 || CoinUtil.compare(betamount, ini.getS_number_high())>0)
						return false;
				}
			} else {
				return false;
			}
		}
		
		BigDecimal allPreBet = CoinUtil.add(tmpAllBet, allBet);
		if (CoinUtil.compare(allPreBet, ini.getAllBet())>0)
			return false;
		
		return true;
	}
		
		
	/*
	 * 机器人上下分，投注处理
	 * 	 	fromID:请求者
	 * 		sendID:发送者
	 * 		groupID:群组ID
	 * 		text:内容
	 */
		public static ErrorCode rebotDeal(String fromID, String sendID,String groupID,String text) {
			if (text==null || text.length()==0)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
			text = Pk10TextMgr.preDeal(text);
			if (text==null || text.length()==0)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】格式不对");
			
			ErrorCode result = null;
			if (Pk10TextMgr.isUpDown(text))
				result = rebotDealUpDown(fromID,sendID, groupID, text);
			else
				result = rebotDealBet(fromID,sendID, groupID, text);
			return result;
		}
		
		/*
		 * 机器人上下分
		 * 	 	fromID:请求者
		 * 		sendID:发送者
		 * 		groupID:群组ID
		 * 		text:内容
		 */
		private static ErrorCode rebotDealUpDown(String fromID, String sendID, String groupID, String text) {

			Pk10PlayerNode player = Pk10RobotMgr.getPlayer(fromID); 
			if (player==null)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】不在线");
			String nickname = player.getNickname();
			
			ComIntBigNode node = Pk10TextMgr.parseUpDown(text);
			if (node==null)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】格式不对 \n【上/下10】");
			
			int type = node.getId();
			BigDecimal amount = node.getMoney();
			if ( 	(type!=Code.AMOUNT_RECHANGE && type!=Code.AMOUNT_WITHDRAWALS) ||
					(amount==null || CoinUtil.compareZero(amount)<=0 ) )
			return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】格式不对");
			
			
			BigDecimal balance = player.getBalance();
			if (type==Code.AMOUNT_WITHDRAWALS) {
				if (CoinUtil.compare(balance, amount)<0)
					return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】余额不足");
				balance = CoinUtil.sub(balance, amount);
			} else {
				balance = CoinUtil.add(balance, amount);
			}
			
			PlayerDao playerDao = DaoMgr.getPlayer();
			//更新余额
			long curTimestamp = DateUtil.getCurTimestamp().getTime();
			player.setBalance(balance);
			player.setUpdatetime(curTimestamp);
			playerDao.updateBalance(player.getId(),balance,curTimestamp);
			

			return new ErrorCode(Code.OK_CODE,"玩家【"+nickname+"】请求【"+text+"】成功,等待后台处理");
		}
		
		/*
		 * 机器人投注
		 * 	 	fromID:请求者
		 * 		sendID:发送者
		 * 		groupID:群组ID
		 * 		text:内容
		 */
		private static ErrorCode rebotDealBet(String fromID, String sendID,String groupID,String text) {
			Pk10PlayerNode player = Pk10RobotMgr.getPlayer(fromID); 
			if (player==null)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+fromID+"】不在线");
			String nickname = player.getNickname();
			
			if (Pk10PrizeMgr.isSealPlate())
				return new ErrorCode(Code.PK10_BET_FAIL,"已封盘");
			Pk10OpenInfo openInfo = Pk10PrizeMgr.getPk10OpenInfo();
			if (openInfo==null)
				return new ErrorCode(Code.PK10_BET_FAIL,"游戏未开始");
			long tmpTime = openInfo.getNextOpentime() - openInfo.getTimestamp();
			if (tmpTime>300000)	//如果下一局相差超过5分钟，不让玩家投注（处理夜间不开奖玩家还能投注的问题）
				return new ErrorCode(Code.PK10_BET_FAIL,"游戏未开始");
			
			
			
	
			Pk10BetTextNode betInfo = Pk10TextMgr.parseBet(text);
			if (betInfo==null)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】格式不对");
			Pk10OpenInfo pk10OpenInfo = Pk10PrizeMgr.getPk10OpenInfo();
			if (pk10OpenInfo==null)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】格式不对");
			String period = pk10OpenInfo.getNextPeriod();
			List<Pk10InfoNode> arrBet = Pk10TextMgr.get(period, betInfo, player.getAppcode(), player.getUsername());
			if (arrBet==null || arrBet.size()==0)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】格式不对");
			
			BigDecimal allBet = Pk10PrizeMgr.getPk10Player().getRealAllBet();
			//下注限制
			if (!betLowHigh(arrBet,allBet))
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】输入金额范围有误");
			
			BigDecimal bet = CoinUtil.zero;
			for (Pk10InfoNode node : arrBet) {
				bet = CoinUtil.add(bet,node.getBetamount());
			}
			
			if (CoinUtil.compare(player.getBalance(),bet)<0)
				return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】投注失败 原因【余额不足】");
			
			
			//获取压注信息
			Pk10Robot pk10Player = Pk10PrizeMgr.getPk10Robot();
			for (Pk10InfoNode node : arrBet) {
				if (!pk10Player.bet(node,player))
					return new ErrorCode(Code.PK10_BET_FAIL,"玩家【"+nickname+"】投注失败");
			}

			String strRel = "玩家【"+nickname+"】投注成功\n";
			for (Pk10InfoNode node : arrBet)
				strRel += node._getBetInfo() + "\n";
			strRel += "当期投注额【"+CoinUtil.Round(bet)+"】";
			
			return new ErrorCode(Code.OK_CODE,strRel);
		}
}

/*
 * PK10投注对象类
 */
package com.pk10.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.pk10.node.Pk10HandicapNode;
import com.sysconst.Code;
import com.util.CoinUtil;
import com.util.DateUtil;

import qht.game.dao.AmountRecordDao;
import qht.game.dao.GameRecordDao;
import qht.game.dao.PeriodRecordDao;
import qht.game.dao.Pk10InfoDao;
import qht.game.dao.PlayerDao;
import qht.game.node.AmountRecord;
import qht.game.node.GameRecord;
import qht.game.node.PK10PaidNode;
import qht.game.node.PeriodRecord;
import qht.game.node.Pk10InfoNode;
import qht.game.node.PlayerNode;

public class Pk10Player {
	private Map<String,Map<String,Map<String,Pk10InfoNode>>> mdata = new HashMap<String,Map<String,Map<String,Pk10InfoNode>>>();//Map<appcode,Map<username,Map<uuid,Pk10InfoNode>>>
     private BigDecimal realAllBet = CoinUtil.zero;
     
     
	public void clear() {
		mdata.clear();
		realAllBet = CoinUtil.zero;
	}
	
	public BigDecimal getRealAllBet() {
		return realAllBet;
	}
	
	/*
	 * 增加投注
	 */
	public void addBet(Pk10InfoNode pk10) {
		String appcode = pk10.getAppcode();
		String username = pk10.getUsername();
		Map<String,Map<String,Pk10InfoNode>> mapAppcode = mdata.get(appcode);
		if (mapAppcode==null) {
			mapAppcode = new HashMap<String,Map<String,Pk10InfoNode>>();
			mdata.put(appcode, mapAppcode);
		}
		Map<String,Pk10InfoNode> mapUser = mapAppcode.get(username);
		if (mapUser==null) {
			mapUser = new HashMap<String,Pk10InfoNode>();
			mapAppcode.put(username, mapUser);
		}
		
		mapUser.put(pk10.getOdd(), pk10);
		//realAllBet = CoinUtil.add(realAllBet, pk10.getBetamount());
	}
	
	/*
	 * 处理压注
	 */
	public boolean bet(Pk10InfoNode pk10,PlayerNode player) {
		BigDecimal bet = pk10.getBetamount();
		String appcode = player.getAppcode();
		String username = player.getUsername();
		Map<String,Map<String,Pk10InfoNode>> mapAppcode = mdata.get(appcode);
		if (mapAppcode==null) {
			mapAppcode = new HashMap<String,Map<String,Pk10InfoNode>>();
			mdata.put(appcode, mapAppcode);
		}
		Map<String,Pk10InfoNode> mapUser = mapAppcode.get(username);
		if (mapUser==null) {
			mapUser = new HashMap<String,Pk10InfoNode>();
			mapAppcode.put(username, mapUser);
		}
		
		PlayerDao playerDao = DaoMgr.getPlayer();
		AmountRecordDao amountRecordDao = DaoMgr.getAmountRecord();
		Pk10InfoDao pk10InfoDao = DaoMgr.getPk10InfoDao();
		if (playerDao==null || amountRecordDao==null || pk10InfoDao==null)
			return false;
		
		BigDecimal balance = player.getBalance();
		BigDecimal aft_bal = balance;
		aft_bal = CoinUtil.sub(balance,bet);
		if (CoinUtil.compareZero(aft_bal)<0)
			return false;

		long curTimestamp = DateUtil.getCurTimestamp().getTime();
		
		//插入投注明细
		if (pk10InfoDao.insert(pk10))
			mapUser.put(pk10.getOdd(), pk10);
		
		//更新余额
		player.setBalance(aft_bal);
		player.setUpdatetime(curTimestamp);
		playerDao.updateBalance(player.getId(),aft_bal,curTimestamp);
		
		
		//插入资金明细
		AmountRecord amountRecord = new AmountRecord();
		amountRecord.setAppcode(player.getAppcode());
		amountRecord.setUsername(player.getUsername());
		amountRecord.setType(Code.AMOUNT_BET);
		amountRecord.setAmount(bet);
		amountRecord.setBef_bal(balance);
		amountRecord.setAft_bal(aft_bal);
		amountRecord.setUpdatetime(curTimestamp);
		amountRecordDao.insert(amountRecord);
		
		if (player.getTypeid()==Code.PLAYER_IDENTIFEY_REAL)
			realAllBet = CoinUtil.add(realAllBet, pk10.getBetamount());
		
		return true;
	}
	
	/*
	 * 处理全部取消压注
	 */
	public boolean cancleBet(PlayerNode player) {
		String appcode = player.getAppcode();
		String username = player.getUsername();
		Map<String,Map<String,Pk10InfoNode>> mapAppcode = mdata.get(appcode);
		if (mapAppcode==null)
			return false;
		Map<String,Pk10InfoNode> mapUser = mapAppcode.get(username);
		if (mapUser==null)
			return false;

		for (Entry<String,Pk10InfoNode> entry : mapUser.entrySet()) {
			Pk10InfoNode value = entry.getValue();
			dealCancleBet(value,player);
		}
		return true;
	}
	
	/*
	 * 处理取消压注
	 */
	public boolean cancleBet(String odd,PlayerNode player) {
		String appcode = player.getAppcode();
		String username = player.getUsername();
		Map<String,Map<String,Pk10InfoNode>> mapAppcode = mdata.get(appcode);
		if (mapAppcode==null)
			return false;
		Map<String,Pk10InfoNode> mapUser = mapAppcode.get(username);
		if (mapUser==null)
			return false;
		Pk10InfoNode pk10 = mapUser.get(odd);
		if (pk10==null)
			return false;
		
		return dealCancleBet(pk10,player);
	}
	
	private boolean dealCancleBet(Pk10InfoNode pk10,PlayerNode player) {

		int status = pk10.getStatus();
		if (status!=Code.PK10_BET)
			return false;

		PlayerDao playerDao = DaoMgr.getPlayer();
		AmountRecordDao amountRecordDao = DaoMgr.getAmountRecord();
		Pk10InfoDao pk10InfoDao = DaoMgr.getPk10InfoDao();
		if (playerDao==null || amountRecordDao==null || pk10InfoDao==null)
			return false;

		long curTimestamp = DateUtil.getCurTimestamp().getTime();
		
		BigDecimal balance = player.getBalance();
		BigDecimal bet = pk10.getBetamount();
		BigDecimal aft_bal = CoinUtil.add(balance,bet);
	
		//将明细类型修改为取消
		pk10.setStatus(Code.PK10_CANCLE);
		pk10InfoDao.updateStatus(pk10.getOdd(), Code.PK10_CANCLE);		

		//更新余额
		player.setBalance(aft_bal);
		player.setUpdatetime(curTimestamp);
		playerDao.updateBalance(player.getId(),aft_bal,curTimestamp);
		
		//插入资金明细
		AmountRecord amountRecord = new AmountRecord();
		amountRecord.setAppcode(player.getAppcode());
		amountRecord.setUsername(player.getUsername());
		amountRecord.setType(Code.AMOUNT_BET_CANCLE);
		amountRecord.setAmount(bet);
		amountRecord.setBef_bal(balance);
		amountRecord.setAft_bal(aft_bal);
		amountRecord.setUpdatetime(curTimestamp);
		amountRecordDao.insert(amountRecord);
		
		if (player.getTypeid()==Code.PLAYER_IDENTIFEY_REAL)
			realAllBet = CoinUtil.sub(realAllBet, pk10.getBetamount());
		
		return true;
	}
	
	/*
	 * 处理开奖
	 */
	public void openCode(PK10PaidNode mpaid, String period, List<Integer> opencode) {
		if (mpaid==null) {
			System.out.println("配表错误");
			return;
		}
		
		PlayerDao playerDao = DaoMgr.getPlayer();
		GameRecordDao gameRecordDao = DaoMgr.getGameRecord();
		AmountRecordDao amountRecordDao = DaoMgr.getAmountRecord();
		PeriodRecordDao periodRecordDao = DaoMgr.getPeriodRecord();
		Pk10InfoDao pk10InfoDao = DaoMgr.getPk10InfoDao();
		if (	playerDao==null || 
				gameRecordDao==null || 
				amountRecordDao==null ||
				periodRecordDao==null ||
						pk10InfoDao==null)
			return;
		
		//计算赔率
		long curTimestamp = DateUtil.getCurTimestamp().getTime();
		int number = 0;
		BigDecimal allBet = CoinUtil.zero, allPaid = CoinUtil.zero;
		BigDecimal bet = CoinUtil.zero, paid = CoinUtil.zero;
		for (Entry<String,Map<String,Map<String,Pk10InfoNode>>> entry1 : mdata.entrySet()) {
			String appcode = entry1.getKey();
			Map<String,Map<String,Pk10InfoNode>> mapAppcode = entry1.getValue();
			for (Entry<String,Map<String,Pk10InfoNode>> entry2 : mapAppcode.entrySet()) {
				String username = entry2.getKey();
				Map<String,Pk10InfoNode> mapUser = entry2.getValue();
				PlayerNode player = PlayerMgr.getByNameWithDB(username);
				if (player==null)
					continue;
				number++;
				bet = CoinUtil.zero;
				paid = CoinUtil.zero;
				for (Entry<String,Pk10InfoNode> entry3 : mapUser.entrySet()) {
					Pk10InfoNode pk10 = entry3.getValue();
					if (!pk10.isBet())
						continue;
					boolean isHit = pk10.settle(opencode, mpaid);
					
					//更新记录状态
					pk10InfoDao.settle(pk10);
					
					bet = CoinUtil.add(bet, pk10.getBetamount());
					if (isHit) {
						//更新余额
						BigDecimal balance = player.getBalance();
						BigDecimal aft_bal = CoinUtil.add(balance, pk10.getPaidamount());
						player.setBalance(aft_bal);
						player.setUpdatetime(curTimestamp);
						playerDao.updateBalance(player.getId(),aft_bal,curTimestamp);
						
						//插入金额变动记录
						AmountRecord amountRecord = new AmountRecord();
						amountRecord.setAppcode(player.getAppcode());
						amountRecord.setUsername(player.getUsername());
						amountRecord.setType(Code.AMOUNT_PAID);
						amountRecord.setAmount(pk10.getPaidamount());
						amountRecord.setBef_bal(balance);
						amountRecord.setAft_bal(aft_bal);
						amountRecord.setUpdatetime(curTimestamp);
						amountRecordDao.insert(amountRecord);
						
						paid = CoinUtil.add(paid, pk10.getPaidamount());
					}
				}
				
				//插入游戏记录
				GameRecord gameRecord = new GameRecord();
				gameRecord.setLotteryid(Code.LORREY_PK10);
				gameRecord.setPeriod(period);
				gameRecord.setAppcode(player.getAppcode());
				gameRecord.setUsername(player.getUsername());
				gameRecord.setTypeid(player.getTypeid());
				gameRecord.setBetamount(bet);
				gameRecord.setPaidamount(paid);
				gameRecord.setUpdatetime(curTimestamp);
				gameRecordDao.insert(gameRecord);
			}
			allBet = CoinUtil.add(allBet, bet);
			allPaid = CoinUtil.add(allPaid, paid);
			
			//插入开盘结算信息
			PeriodRecord periodRecord = new PeriodRecord();
			periodRecord.setPeriod(period);
			periodRecord.setAppcode(appcode);
			periodRecord.setLorreyid(Code.LORREY_PK10);
			periodRecord.setNumber(number);
			periodRecord.setBet(allBet);
			periodRecord.setPaid(allPaid);
			periodRecord.setUpdatetime(curTimestamp);
			periodRecordDao.insert(periodRecord);
			
			number = 0;
			allBet = CoinUtil.zero;
			allPaid = CoinUtil.zero;
		}
	}
	
	/*
	 * 处理飞盘
	 */
	public List<Pk10HandicapNode> dealHandicap() {
		Pk10InfoDao dao = DaoMgr.getPk10InfoDao();
		
		List<Pk10HandicapNode> result = new ArrayList<Pk10HandicapNode>();
		for (Entry<String,Map<String,Map<String,Pk10InfoNode>>> entry : mdata.entrySet()) {
			Map<String,Map<String,Pk10InfoNode>> value = entry.getValue();
			for (Entry<String,Map<String,Pk10InfoNode>> entry2 : value.entrySet()) {
				Map<String,Pk10InfoNode> value2 = entry2.getValue();
				for (Entry<String,Pk10InfoNode> entry3 : value2.entrySet()) {
					Pk10InfoNode node = entry3.getValue();
					
					if (node.getStatus()==Code.PK10_BET) {
						if (dao.updateStatus(node.getOdd(),Code.PK10_HANDICAP)) {
							node.setStatus(Code.PK10_HANDICAP);
							
							Pk10HandicapNode info = new Pk10HandicapNode();
							info.setOdd(node.getOdd());
							info.setPeriod(node.getPeriod());
							info.setRunway(node.getRunway());
							info.setBettype(node.getBettype());
							info.setBetamount(node.getBetamount());
							result.add(info);
						}
					}
				}
			}
		}
		return result;
	}
}

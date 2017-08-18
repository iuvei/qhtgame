/*
 * PK10机器人投注对象类
 */
package com.pk10.logic;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.pk10.node.Pk10OpenInfo;
import com.util.CoinUtil;
import com.util.DateUtil;

import qht.game.dao.Pk10PlayerDao;
import qht.game.node.PK10PaidNode;
import qht.game.node.Pk10InfoNode;
import qht.game.node.Pk10PlayerNode;
import qht.game.node.PlayerNode;

public class Pk10Robot {
	private Map<String,Map<String,Map<String,Pk10InfoNode>>> mdata = new HashMap<String,Map<String,Map<String,Pk10InfoNode>>>();//Map<appcode,Map<username,Map<uuid,Pk10InfoNode>>>

	public void clear() {
		mdata.clear();
	}
	
	//处理压注
	public boolean bet(Pk10InfoNode pk10,Pk10PlayerNode player) {
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
		
		Pk10PlayerDao playerDao = DaoMgr.getPk10Player();
		if (playerDao==null)
			return false;
		
		long curTimestamp = DateUtil.getCurTimestamp().getTime();
		
		BigDecimal balance = player.getBalance();
		if (CoinUtil.compare(balance, bet)<0) 
				return false;
		
		BigDecimal aft_bal = CoinUtil.sub(balance,bet);
		if (CoinUtil.compareZero(aft_bal)<0)
			return false;

		//插入投注明细
		mapUser.put(pk10.getOdd(), pk10);
		
		//更新余额
		player.setBalance(aft_bal);
		player.setUpdatetime(curTimestamp);
		playerDao.updateBalance(player.getUsername(),aft_bal,curTimestamp);
		
		return true;
	}
	
	//处理开奖
	public void openCode(PK10PaidNode mpaid, Pk10OpenInfo info) {
		if (mpaid==null) {
			System.out.println("配表错误");
			return;
		}
		
		Pk10PlayerDao playerDao = DaoMgr.getPk10Player();
		if (playerDao==null)
			return;
		
		//计算赔率
		long curTimestamp = DateUtil.getCurTimestamp().getTime();
		for (Entry<String,Map<String,Map<String,Pk10InfoNode>>> entry1 : mdata.entrySet()) {
			Map<String,Map<String,Pk10InfoNode>> mapAppcode = entry1.getValue();
			for (Entry<String,Map<String,Pk10InfoNode>> entry2 : mapAppcode.entrySet()) {
				String username = entry2.getKey();
				Map<String,Pk10InfoNode> mapUser = entry2.getValue();
				PlayerNode player = PlayerMgr.getByNameWithDB(username);
				if (player==null)
					continue;
				for (Entry<String,Pk10InfoNode> entry3 : mapUser.entrySet()) {
					Pk10InfoNode pk10 = entry3.getValue();
					pk10.settle(info.getOpencode(), mpaid);
					
					//更新余额
					BigDecimal balance = player.getBalance();
					BigDecimal aft_bal = CoinUtil.add(balance, pk10.getPaidamount());
					player.setBalance(aft_bal);
					player.setUpdatetime(curTimestamp);
					playerDao.updateBalance(player.getUsername(),aft_bal,curTimestamp);
				}
			}
		}
	}	
}

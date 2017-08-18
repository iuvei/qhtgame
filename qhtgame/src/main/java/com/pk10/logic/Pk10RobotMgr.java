/*
 * Pk10机器人管理类
 */
package com.pk10.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logic.mgr.DaoMgr;
import com.node.ErrorCode;
import com.pk10.node.Pk10OpenInfo;
import com.pk10.node.Pk10RebotBetNode;
import com.pk10.node.Pk10RebotScheme;
import com.sysconst.Code;
import com.tls.sigcheck.ImOpr;
import com.tls.sigcheck.TlsSig;
import com.util.CoinUtil;
import com.util.DateUtil;
import com.util.RandUtil;

import qht.game.dao.Pk10PlayerDao;
import qht.game.dao.RebotRecordDao;
import qht.game.dao.RebotSchemeDao;
import qht.game.node.Pk10PlayerNode;
import qht.game.node.Pk10PlayerNodeDB;
import qht.game.node.Pk10RebotSchemeDB;
import qht.game.node.RebotRecordNode;
import qht.game.node.RebotSchemeNode;

public class Pk10RobotMgr {
	
	//机器人对象集
	private static Map<String,Pk10PlayerNode> mplayer = new HashMap<String,Pk10PlayerNode>();
	//在线人数
	private static int onlineCount = 0;
	//方案对象集
	private static Map<String,Pk10RebotScheme> mscheme = new HashMap<String,Pk10RebotScheme>();
	//每局收集的投注集
	private static List<Pk10RebotBetNode> mbet = new ArrayList<Pk10RebotBetNode>();
	
	
	public static boolean init() {
		//加载机器人对象
		List<Pk10PlayerNodeDB> listPlayer = DaoMgr.getPk10Player().selectList();
		if (listPlayer==null)
			return false;
		for (Pk10PlayerNodeDB node : listPlayer) {
			List<RebotSchemeNode> schemes = new Gson().fromJson(new String(node.getSchemes()), new TypeToken<List<RebotSchemeNode>>(){}.getType());
			Pk10PlayerNode info = new Pk10PlayerNode();
			info.setId(node.getId());
			info.setAppcode(node.getAppcode());
			info.setUsername(node.getUsername());
			info.setNickname(node.getNickname());
			info.setStatus(node.getStatus());
			info.setBalance(node.getBalance());
			info.setSchemes(schemes);
			info.setCreatetime(node.getCreatetime());
			info.setUpdatetime(node.getUpdatetime());
			
			mplayer.put(info.getUsername(), info);
			if (node.getStatus()==Code.STATUS_EFFECTIVE)
				onlineCount++;
		}
		
		//加载方案集
		List<Pk10RebotSchemeDB> listScheme = DaoMgr.getRebotScheme().selectList();
		if (listScheme==null)
			return false;
		for (Pk10RebotSchemeDB node : listScheme) {
			Pk10RebotScheme info = new Pk10RebotScheme();
			info.setId(node.getId());
			info.setName(node.getName());
			info.setLow_amount(node.getLow_amount());
			info.setSendup_amount(node.getSendup_amount());
			info.setUp_amount(node.getUp_amount());
			info.setSenddown_amount(node.getSenddown_amount());
			info.setStop_amount(node.getStop_amount());
			List<String> arrText = new Gson().fromJson(new String(node.getSend_text()), new TypeToken<List<String>>(){}.getType());
			info.setSend_text(arrText);
			info.setUpdatetime(node.getUpdatetime());
			info.setCreatetime(node.getCreatetime());
			
			mscheme.put(info.getName(), info);
		}
		return true;
	}
	public static Pk10PlayerNode getPlayer(String username) {
		return mplayer.get(username);
	}
	public static Pk10RebotScheme getScheme(String name) {
		return mscheme.get(name);
	}
	public static List<String> getSchemeNames() {
		List<String> result = new ArrayList<String>();
		for (Entry<String,Pk10RebotScheme> entry : mscheme.entrySet()) {
			result.add(entry.getKey());
		}
		return result;
	}
	/*
	 * 获取在经人数
	 */
	public static int getOnlineCount() {
		return onlineCount;
	}
	
	/*
	 * 结算的时候初始化方案
	 */
	public static void roundInit() {
		mbet.clear();
		for (Entry<String,Pk10PlayerNode> entry : mplayer.entrySet()) {
			Pk10PlayerNode playerNode = entry.getValue();
			if (playerNode.getStatus()!=Code.STATUS_EFFECTIVE)
				continue;
			List<RebotSchemeNode> arrScheme = playerNode.getSchemes();
			for (RebotSchemeNode node : arrScheme) {
				int probabi = node.getProbabi();
				if (probabi<0 || probabi>100)
					continue;
				int rand = RandUtil.randInt(1,100);
				if (rand<=probabi) {
					String scheme = node.getScheme();
					Pk10RebotScheme schemeNode = mscheme.get(scheme);
					if (schemeNode==null)
						continue;
					
					BigDecimal balance = playerNode.getBalance();
					BigDecimal low_amount = schemeNode.getLow_amount();			//剩余金额低于此值
					BigDecimal sendup_amount = schemeNode.getSendup_amount();		//发送上分金额
					BigDecimal up_amount = schemeNode.getUp_amount();			//剩余金额高于此值
					BigDecimal senddown_amount = schemeNode.getSenddown_amount();		//发送下分金额
					BigDecimal stop_amount = schemeNode.getStop_amount();			//金额小于此值，停止投注
					
					if (CoinUtil.compare(balance, low_amount)<0) {
						String text = "上"+CoinUtil.Round(sendup_amount);
						int time = RandUtil.randInt(120,250);
						mbet.add(new Pk10RebotBetNode(playerNode.getUsername(),playerNode.getNickname(),text,time));
					}
					if (CoinUtil.compare(balance, up_amount)>0) {
						String text = "下"+CoinUtil.Round(senddown_amount);
						int time = RandUtil.randInt(120,250);
						mbet.add(new Pk10RebotBetNode(playerNode.getUsername(),playerNode.getNickname(),text,time));
					}
					if (CoinUtil.compare(balance, stop_amount)<0)
						continue;
					
					
					List<String> sendText = schemeNode.getSend_text();
					if (sendText==null || sendText.size()==0)
						continue;
					int count = sendText.size();
					rand = RandUtil.randInt(0,count-1);
					String text = sendText.get(rand);
					if (text!=null) {
						mbet.add(new Pk10RebotBetNode(playerNode.getUsername(),playerNode.getNickname(),text,node.getTime()));
					}
				}
			}
		}
	}

	/*
	 * 机器人投注动作（定时）
	 */
	public static void timer() {
		Pk10OpenInfo info = Pk10PrizeMgr.getPk10OpenInfo();
		if (info==null)
			return;
		long mytimer = info.getNextOpentime() - info.getTimestamp();
		int remainTimer = (int)(mytimer/1000);
		if (remainTimer>=300 || remainTimer<=0)
			return;
		String period = info.getNextPeriod();
		
		try {
			List<Pk10RebotBetNode> _mmbet = new ArrayList<Pk10RebotBetNode>();
			for (Pk10RebotBetNode node : mbet) {
				if (node.getTime()>remainTimer) {
					_mmbet.add(node);
					ErrorCode result = Pk10OprMgr.rebotDeal(node.getUsername(),TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, node.getText());
					if (result!=null && result.getCode()==Code.OK_CODE) {
						ImOpr.sendMsg(node.getUsername(), TlsSig.PK10_GROUP_ID, node.getText(), false);
						Thread.sleep(2000);
						ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, result.getDesc(), false);
						//ImOpr.sendGroupMsgSystem(GroupId, From_Account, respone);
					
						RebotRecordNode record = new RebotRecordNode();
						record.setUsername(node.getUsername());
						record.setNickname(node.getNickname());
						record.setPeriod(period);
						record.setText(node.getText());
						record.setStatus(Code.STATUS_EFFECTIVE);
						record.setUpdatetime(DateUtil.getCurTimestamp().getTime());
						RebotRecordDao dao = DaoMgr.getPk10RebotRecord();
						dao.insert(record);
						Thread.sleep(2000);
						continue;
					} else {
						RebotRecordNode record = new RebotRecordNode();
						record.setUsername(node.getUsername());
						record.setNickname(node.getNickname());
						record.setPeriod(period);
						record.setText(node.getText());
						record.setStatus(Code.STATUS_UNEFFECTIVE);
						record.setUpdatetime(DateUtil.getCurTimestamp().getTime());
						RebotRecordDao dao = DaoMgr.getPk10RebotRecord();
						dao.insert(record);
						continue;
					}
				}
			}
			
			for (Pk10RebotBetNode node : _mmbet) {
				mbet.remove(node);
			}
		} catch (InterruptedException e) {}
	}
	
	
	
	
	
	/*
	 * 添加机器人
	 */
	public static boolean addPlayer(Pk10PlayerNode info) {
		String username = info.getUsername();
		if (username==null || username.length()==0)
			return false;
		for (Entry<String,Pk10PlayerNode> entry : mplayer.entrySet()) {
			Pk10PlayerNode node = entry.getValue();
			if (username.equals(node.getUsername()))
				return false;
		}
		
		Pk10PlayerDao dao = DaoMgr.getPk10Player();
		if (!dao.insert(info))
			return false;
		Pk10PlayerNodeDB _info = dao.selectOne(username);
		if (_info==null)
			return false;
		
		List<RebotSchemeNode> schemes = new Gson().fromJson(new String(_info.getSchemes()), new TypeToken<List<RebotSchemeNode>>(){}.getType());
		Pk10PlayerNode newInfo = new Pk10PlayerNode();
		newInfo.setId(_info.getId());
		newInfo.setAppcode(_info.getAppcode());
		newInfo.setUsername(_info.getUsername());
		newInfo.setNickname(_info.getNickname());
		newInfo.setStatus(_info.getStatus());
		newInfo.setBalance(_info.getBalance());
		newInfo.setSchemes(schemes);
		newInfo.setCreatetime(_info.getCreatetime());
		newInfo.setUpdatetime(_info.getUpdatetime());
		
		mplayer.put(newInfo.getUsername(), newInfo);
		
		return true;
	}
	
	/*
	 * 更新机器人
	 */
	public static boolean updatePlayer(String username, String nickname, int status) {
		if (	username==null || username.length()==0 ||
				nickname==null || nickname.length()==0 ||
				(status!=Code.STATUS_EFFECTIVE && status!=Code.STATUS_UNEFFECTIVE)
				)
			return false;
		
		Pk10PlayerNode info = mplayer.get(username);
		 if (info==null)
			 return false;
		 
		long tmpTime = DateUtil.getCurTimestamp().getTime();
		Pk10PlayerDao dao = DaoMgr.getPk10Player();
		if (!dao.update(username,nickname,status,tmpTime))
			return false;
		
		info.setNickname(nickname);
		info.setStatus(status);
		info.setUpdatetime(tmpTime);
		
		return true;
	}
	
	/*
	 * 机器人添加投注
	 */
	public static boolean addScheme(String username, RebotSchemeNode schemeNode) {
		if (	username==null || username.length()==0 || schemeNode==null )
			return false;
		
		Pk10PlayerNode info = mplayer.get(username);
		 if (info==null)
			 return false;
		 
		 List<RebotSchemeNode> schemes = info.getSchemes();
		 if (schemes==null)
			 return false;
		 
		 for (RebotSchemeNode node : schemes) {	//是否已经包含了
			 if (node.getId()==schemeNode.getId())
				 return false;
		 }
		 
		 if (!schemes.add(schemeNode))
			 return false;
		 
		long tmpTime = DateUtil.getCurTimestamp().getTime();
		Pk10PlayerDao dao = DaoMgr.getPk10Player();
		if (!dao.update2(username,schemes,tmpTime)) {
			schemes.remove(schemeNode);
			return false;
		}
		
		info.setSchemes(schemes);
		info.setUpdatetime(tmpTime);
		
		return true;
	}
	
	/*
	 * 机器人删除投注
	 */
	public static boolean delScheme(String username, int id) {
		if (	username==null || username.length()==0 || id<=0	)
			return false;
		
		Pk10PlayerNode info = mplayer.get(username);
		 if (info==null)
			 return false;
		 
		 List<RebotSchemeNode> schemes = info.getSchemes();
		 if (schemes==null)
			 return false;
		 
		 RebotSchemeNode schemeNode = null;
		 for (RebotSchemeNode node : schemes) {	//是否已经包含了
			 if (node.getId()==id) {
				 schemeNode = node;
				 break;
			 }
		 }
		 if (schemeNode==null)
			 return false;
		 
		 if (!schemes.remove(schemeNode))
			 return false;
		 
		long tmpTime = DateUtil.getCurTimestamp().getTime();
		Pk10PlayerDao dao = DaoMgr.getPk10Player();
		if (!dao.update2(username,schemes,tmpTime)) {
			schemes.add(schemeNode);
			return false;
		}
		
		info.setSchemes(schemes);
		info.setUpdatetime(tmpTime);
		
		return true;
	}
	
	/*
	 * 获取方案集
	 */
	public static List<Pk10RebotScheme> getSchemeAll() {
		List<Pk10RebotScheme> data = new ArrayList<Pk10RebotScheme>();
		for (Entry<String,Pk10RebotScheme> entry : mscheme.entrySet()) {
			data.add(entry.getValue());
		}
		return data;
	}
	
	/*
	 * 添加方案集
	 */
	public static boolean addScheme(Pk10RebotScheme info) {
		String name = info.getName();
		if (name==null || name.length()==0)
			return false;
		for (Entry<String,Pk10RebotScheme> entry : mscheme.entrySet()) {
			Pk10RebotScheme node = entry.getValue();
			if (name.equals(node.getName()))
				return false;
		}
		
		RebotSchemeDao dao = DaoMgr.getRebotScheme();
		if (!dao.insert(info))
			return false;
		Pk10RebotSchemeDB _info = dao.selectOne(name);
		if (_info==null)
			return false;
		
		Pk10RebotScheme newInfo = new Pk10RebotScheme();
		newInfo.setId(_info.getId());
		newInfo.setName(_info.getName());
		newInfo.setLow_amount(_info.getLow_amount());
		newInfo.setSendup_amount(_info.getSendup_amount());
		newInfo.setUp_amount(_info.getUp_amount());
		newInfo.setSenddown_amount(_info.getSenddown_amount());
		newInfo.setStop_amount(_info.getStop_amount());
		List<String> arrText = new Gson().fromJson(new String(_info.getSend_text()), new TypeToken<List<String>>(){}.getType());
		newInfo.setSend_text(arrText);
		newInfo.setUpdatetime(_info.getUpdatetime());
		newInfo.setCreatetime(_info.getCreatetime());
		
		mscheme.put(newInfo.getName(), newInfo);
		
		return true;
	}
	
	/*
	 * 更新方案内容
	 */
	public static boolean updateScheme(String name, BigDecimal low_amount,BigDecimal sendup_amount,BigDecimal up_amount,BigDecimal senddown_amount,BigDecimal stop_amount) {
		if (	name==null || name.length()==0 ||
				low_amount==null || CoinUtil.compareZero(low_amount)<=0 ||
				sendup_amount==null || CoinUtil.compareZero(sendup_amount)<=0 ||
				up_amount==null || CoinUtil.compareZero(up_amount)<=0 ||
				senddown_amount==null || CoinUtil.compareZero(senddown_amount)<=0 ||
				stop_amount==null || CoinUtil.compareZero(stop_amount)<=0
				)
			return false;
		
		 Pk10RebotScheme info = mscheme.get(name);
		 if (info==null)
			 return false;
		 
		long tmpTime = DateUtil.getCurTimestamp().getTime();
		RebotSchemeDao dao = DaoMgr.getRebotScheme();
		if (!dao.update1(name,low_amount,sendup_amount,up_amount,senddown_amount,stop_amount,tmpTime))
			return false;
		
		info.setLow_amount(low_amount);
		info.setSendup_amount(sendup_amount);
		info.setUp_amount(up_amount);
		info.setSenddown_amount(senddown_amount);
		info.setStop_amount(stop_amount);
		info.setUpdatetime(tmpTime);
		
		return true;
	}
	
	/*
	 * 添加投注内容
	 */
	public static boolean addText(String name, String text) {
		if (	name==null || name.length()==0 ||
				text==null || text.length()==0
				)
			return false;
		
		 Pk10RebotScheme info = mscheme.get(name);
		 if (info==null)
			 return false;
		 
		 List<String> arrText = info.getSend_text();
		 if (arrText==null)
			 return false;
		 
		 if (arrText.contains(text))	//已经包含了
			 return false;
		 
		 if (!arrText.add(text))
			 return false;
		 
		long tmpTime = DateUtil.getCurTimestamp().getTime();
		RebotSchemeDao dao = DaoMgr.getRebotScheme();
		if (!dao.update2(name,arrText,tmpTime)) {
			arrText.remove(text);
			return false;
		}
		
		info.setSend_text(arrText);
		info.setUpdatetime(tmpTime);
		
		return true;
	}
	
	/*
	 * 删除投注内容
	 */
	public static boolean delText(String name, String text) {
		if (	name==null || name.length()==0 ||
				text==null || text.length()==0
				)
			return false;
		
		 Pk10RebotScheme info = mscheme.get(name);
		 if (info==null)
			 return false;
		 
		 List<String> arrText = info.getSend_text();
		 if (arrText==null)
			 return false;
		 
		 if (!arrText.contains(text))	//不包含
			 return false;
		 
		 if (!arrText.remove(text))
			 return false;
		 
		long tmpTime = DateUtil.getCurTimestamp().getTime();
		RebotSchemeDao dao = DaoMgr.getRebotScheme();
		if (!dao.update2(name,arrText,tmpTime)) {
			arrText.add(text);
			return false;
		}
		
		info.setSend_text(arrText);
		info.setUpdatetime(tmpTime);
		
		return true;
	}
}
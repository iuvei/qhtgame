/*
 * 玩家管理集
 */
package com.logic.mgr;

import java.util.HashMap;
import java.util.Map;

import com.node.UserInfo;

import qht.game.dao.PlayerDao;
import qht.game.node.PlayerNode;

public class PlayerMgr {

	private static Map<String,String> pdata = new HashMap<String,String>();	//Map<sessionid,username>
	private static Map<String,UserInfo> udata = new HashMap<String,UserInfo>();	//Map<usernmae,UserInfo>
	
	/*
	 * 添加玩家对象
	 */
	public static synchronized void put(UserInfo user) {
		pdata.put(user.getSessionID(), user.getUsername());
		udata.put(user.getUsername(), user);
	}
	
	/*
	 * 获取玩家个数
	 */
	public static synchronized int getCount() {
		return pdata.size();
	}
	
	/*
	 * 根据用户名获取玩家对象，取不到从数据库拿
	 */
	public static synchronized PlayerNode getByNameWithDB(String username) {
		UserInfo userInfo = udata.get(username);
		if (userInfo!=null)
			return userInfo.getPlayer();
		PlayerDao dao = DaoMgr.getPlayer();
		return dao.selectOneByName(username);
	}
	
	/*
	 * 根据用户名获取玩家对象
	 */
	public static synchronized UserInfo getByName(String username) {
		return udata.get(username);
	}
	
	/*
	 * 根据sessionid获取玩家对象
	 */
	public static synchronized UserInfo getBySessionID(String sessionID) {
		String username = pdata.get(sessionID);
		if (username==null)
			return null;
		return udata.get(username);
	}

	/*
	 * 根据用户名删除玩家对象
	 */
	public static synchronized UserInfo removeByName(String username) {
		UserInfo info = udata.remove(username);
		if (info==null)
			return null;
		String sessionID = info.getSessionID();
		if (sessionID!=null)
			pdata.remove(sessionID);
		return info;
	}
	
	/*
	 * 根据sessionID删除玩家对象
	 */
	public static synchronized UserInfo removeBySessionID(String sessionID) {
		String username = pdata.remove(sessionID);
		if (username==null)
			return null;
		return udata.remove(username);
	}
}

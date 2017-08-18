/*
 * 后台玩家集
 */
package com.logic.mgr;

import java.util.HashMap;
import java.util.Map;

import com.node.AdminInfo;

public class AdminMgr {

	private static Map<String,String> pdata = new HashMap<String,String>();	//Map<sessionid,username>
	private static Map<String,AdminInfo> udata = new HashMap<String,AdminInfo>();	//Map<usernmae,UserInfo>
	
	/*
	 * 添加Admin
	 */
	public static synchronized void put(AdminInfo admin) {
		pdata.put(admin.getSessionID(), admin.getUsername());
		udata.put(admin.getUsername(), admin);
	}
	
	/*
	 * 根据用户名获得Admin
	 */
	public static synchronized AdminInfo getByName(String username) {
		return udata.get(username);
	}
	
	/*
	 * 根据sessionid获得Admin
	 */
	public static synchronized AdminInfo getBySessionID(String sessionID) {
		String username = pdata.get(sessionID);
		if (username==null)
			return null;
		return udata.get(username);
	}

	/*
	 * 根据用户名删除Admin
	 */
	public static synchronized AdminInfo removeByName(String username) {
		AdminInfo info = udata.remove(username);
		if (info==null)
			return null;
		String sessionID = info.getSessionID();
		if (sessionID!=null)
			pdata.remove(sessionID);
		return info;
	}
	
	/*
	 * 根据sessionid删除admin
	 */
	public static synchronized AdminInfo removeBySessionID(String sessionID) {
		String username = pdata.remove(sessionID);
		if (username==null)
			return null;
		return udata.remove(username);
	}
}

/*
 * app管理集
 */
package com.logic.mgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qht.game.node.AppinfoNode;

public class AppinfoMgr {
	private static Map<String,AppinfoNode> mdata = new HashMap<String,AppinfoNode>();
	
	public static boolean init() {
		List<AppinfoNode> list = DaoMgr.getAppinfo().selectList("");
		if (list==null)
			return false;
		
		for (AppinfoNode node : list) {
			mdata.put(node.getAppcode(), node);
		}
		return true;
	}

	public static AppinfoNode get(String appcode) {
		return mdata.get(appcode);
	}
	
	public static void put(AppinfoNode node) {
		mdata.put(node.getAppcode(), node);
	}

}

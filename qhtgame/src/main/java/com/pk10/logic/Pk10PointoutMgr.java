/*
 * 超级账号定时在群里发公告
 */
package com.pk10.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.logic.mgr.DaoMgr;
import com.tls.sigcheck.ImOpr;
import com.tls.sigcheck.TlsSig;

import qht.game.node.Pk10PointoutNode;

public class Pk10PointoutMgr {

	private static Map<Integer,Map<Integer,Pk10PointoutNode>> mdata = new HashMap<Integer,Map<Integer,Pk10PointoutNode>>();
	private static Map<Integer,Map<Integer,Pk10PointoutNode>> ldata = new HashMap<Integer,Map<Integer,Pk10PointoutNode>>();
	
	public static boolean init() {
		List<Pk10PointoutNode> list = DaoMgr.getPk10Pointout().selectList();
		for (Pk10PointoutNode node : list) {
			int lottery_id = node.getLottery_id();
			Map<Integer,Pk10PointoutNode> node1 = mdata.get(lottery_id);
			if (node1==null) {
				node1 = new HashMap<Integer,Pk10PointoutNode>();
				mdata.put(lottery_id, node1);
			}
			int text_id = node.getText_id();
			node1.put(text_id, node);			
		}
		return true;
	}
	
	public static Pk10PointoutNode get(int lottery_id,int text_id) {
		Map<Integer,Pk10PointoutNode> node = mdata.get(lottery_id);
		if (node==null)
			return null;
		return node.get(text_id);
	}

	public static void add(Pk10PointoutNode node) {
		int lottery_id = node.getLottery_id();
		Map<Integer,Pk10PointoutNode> node1 = mdata.get(lottery_id);
		if (node1==null) {
			node1 = new HashMap<Integer,Pk10PointoutNode>();
			mdata.put(lottery_id, node1);
		}
		int text_id = node.getText_id();
		node1.put(text_id, node);
	}
	
	public static Pk10PointoutNode del(int lottery_id,int text_id) {
		Map<Integer,Pk10PointoutNode> node = mdata.get(lottery_id);
		if (node==null)
			return null;
		return node.remove(text_id);
	}
	
	/*
	 * opentime:距开盘时间
	 */
	public static void timer(int opentime) {
		List<Integer> removeStr = new ArrayList<Integer>();
		for (Entry<Integer,Map<Integer,Pk10PointoutNode>> entry : ldata.entrySet()) {
			removeStr.clear();
			Map<Integer,Pk10PointoutNode> _list = entry.getValue();
			for (Entry<Integer,Pk10PointoutNode> entry2 : _list.entrySet()) {
				Pk10PointoutNode node = entry2.getValue();
				if (opentime<node.getSpacetime()) {
					ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, new String(node.getText()), false);
					removeStr.add(node.getText_id());
				}
			}
			
			for (Integer id : removeStr) {
				_list.remove(id);
			}
		}
	}
	
	public static void roundInit() {
		ldata.clear();
		for (Entry<Integer, Map<Integer, Pk10PointoutNode>> entry : mdata.entrySet()) {
			int lottery_id = entry.getKey();
			Map<Integer, Pk10PointoutNode> _mapData = entry.getValue();
			
			Map<Integer,Pk10PointoutNode> _list = ldata.get(lottery_id);
			if (_list==null) {
				_list = new HashMap<Integer,Pk10PointoutNode>();
				ldata.put(lottery_id, _list);
			}
			
			for (Entry<Integer, Pk10PointoutNode> entry2 : _mapData.entrySet()) {
				Pk10PointoutNode node = entry2.getValue();
				_list.put(node.getText_id(),node);
			}
		}
	}
}

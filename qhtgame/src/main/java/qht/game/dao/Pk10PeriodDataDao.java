/*
 * pk10期数信接口
 * qht_pk10_period_data
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.util.Dao;

import qht.game.node.Pk10PeriodDataInfo;
import qht.game.node.Pk10PeriodDataNode;

public class Pk10PeriodDataDao{
	
	public boolean insert(Pk10PeriodDataInfo info) {
		if (1!=Dao.insert("pk10perioddataInsert",info.getParameter()))
			return false;
		return true;
	}

	public Pk10PeriodDataInfo selectOne(String period) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("period", period);
		Pk10PeriodDataNode node = (Pk10PeriodDataNode) Dao.selectOne("pk10perioddataSelectOne", parameter);
		if (node==null)
			return null;
		String str = node.getOpencode();
		List<Integer> opencode = new Gson().fromJson(str, new TypeToken<List<Integer>>() {}.getType());
		Pk10PeriodDataInfo info = new Pk10PeriodDataInfo();
		info.setPeriod(node.getPeriod());
		info.setOpencode(opencode);
		info.setOpentime(node.getOpentime());
		
		return info;
	}

	@SuppressWarnings("unchecked")
	public List<Pk10PeriodDataNode> selectList(Map<String, Object> parameter) {
		return (List<Pk10PeriodDataNode>) Dao.selectList("pk10perioddataSelectList", parameter);

	}
	
}

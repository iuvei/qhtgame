/*
 * pk10群提示接口
 * qht_pk10_pointout
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;
import com.util.DateUtil;

import qht.game.node.Pk10PointoutNode;

public class Pk10Pointout {

	@SuppressWarnings("unchecked")
	public List<Pk10PointoutNode> selectList() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("lottery_id", 0);
		return (List<Pk10PointoutNode>) Dao.selectList("pk10pointoutselectlist", parameter);
	}
	
	@SuppressWarnings("unchecked")
	public List<Pk10PointoutNode> selectList(int lottery_id) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("lottery_id", lottery_id);
		return (List<Pk10PointoutNode>) Dao.selectList("pk10pointoutselectlist", parameter);
	}

	public boolean insert(int lottery_id, int text_id, int spacetime, String text) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("lottery_id", lottery_id);
		parameter.put("text_id", text_id);
		parameter.put("spacetime", spacetime);
		parameter.put("text", text.getBytes());
		parameter.put("createtime", DateUtil.getCurTimestamp().getTime());
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		if (1!=Dao.insert("pk10pointoutinsertone", parameter))
			return false;
		return true;
	}

	public Pk10PointoutNode selectOne(int lottery_id, int text_id) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("lottery_id", lottery_id);
		parameter.put("text_id", text_id);
		return (Pk10PointoutNode) Dao.selectOne("pk10pointoutselectone", parameter);
	}

	public boolean edit(int lottery_id,int text_id, int spacetime, String text) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("lottery_id", lottery_id);
		parameter.put("text_id", text_id);
		parameter.put("spacetime", spacetime);
		parameter.put("text", text);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		if (1!=Dao.update("pk10pointoutupdateone", parameter))
			return false;
		return true;
	}

	public boolean delete(int lottery_id,int text_id) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("lottery_id", lottery_id);
		parameter.put("text_id", text_id);
		if (1!=Dao.delete("pk10pointoutdeleteone", parameter))
			return false;
		return true;
	}
}

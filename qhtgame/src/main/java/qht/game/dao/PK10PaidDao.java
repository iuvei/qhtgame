/*
 * pk10赔率
 * qht_pk10_paid_ini
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.PK10BetNode;
import qht.game.node.PK10PaidNode;

public class PK10PaidDao {

	@SuppressWarnings("unchecked")
	public List<PK10PaidNode> selectList(int paid_id) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", paid_id);
		return (List<PK10PaidNode>) Dao.selectList("paidSelectList", parameter);
	}

	public boolean editInfo(Map<String, Object> parameter) {
		if (1!=Dao.update("paidEditInfo",parameter))
			return false;
		return true;
	}
	public boolean editInfoBet(Map<String, Object> parameter) {
		if (1!=Dao.update("betEditInfo",parameter))
			return false;
		return true;
	}
	
	public PK10PaidNode selectOne(int paid_id) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", paid_id);
		return (PK10PaidNode) Dao.selectOne("paidSelectOne", parameter);
	}
	
	public PK10BetNode selectOneBet(int paid_id) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", paid_id);
		return (PK10BetNode) Dao.selectOne("betSelectOne", parameter);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PK10BetNode> selectBetAll() {
		return (List<PK10BetNode>) Dao.selectList("selectBetAll", null);
	}
}

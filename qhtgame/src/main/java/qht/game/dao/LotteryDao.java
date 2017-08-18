/*
 * 彩种信息接口
 * qht_lottery
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.ComUtil;
import com.util.Dao;
import com.util.DateUtil;

import qht.game.node.LotteryNode;

public class LotteryDao {

	@SuppressWarnings("unchecked")
	public List<LotteryNode> selectList(int lottery_id) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", lottery_id);
		return (List<LotteryNode>) Dao.selectList("lotterySelectList", parameter);
	}

	public boolean setTime(Map<String, Object> parameter) {
		if (1!=Dao.update("lotterySetTime",parameter))
			return false;
		return true;
	}

	public LotteryNode selectOne(int id) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		return (LotteryNode) Dao.selectOne("lotterySelectOne", parameter);
	}
	
	public boolean insert(Map<String, Object> parameter) {
		if (1!=Dao.insert("lotteryInsert", parameter))
			return false;
		return true;
	}
	
	public boolean edit(int lottery_id, String lottery_rule) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("lottery_id", lottery_id);
		parameter.put("lottery_rule", ComUtil.encode(lottery_rule.getBytes()));
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		if (1!=Dao.update("lotteryeditrule", parameter))
			return false;
		return true;
	}
	
}

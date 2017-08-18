/*
 * 日报接口
 * qht_report_day
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.ReportBetPaidByDayNode;
import qht.game.node.ReportNode;

public class ReportDao {
	
	public ReportBetPaidByDayNode countByDay(int date) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("date", date);
		return (ReportBetPaidByDayNode) Dao.selectOne("reportdayCountByDay", parameter);
	}

	@SuppressWarnings("unchecked")
	public List<ReportNode> selectList(Map<String, Object> parameter) {
		return (List<ReportNode>) Dao.selectList("reportdaySelectList", parameter);
	}

	public boolean insert(ReportNode record) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("date", record.getDate());
		parameter.put("appcode", record.getAppcode());
		parameter.put("username", record.getUsername());
		parameter.put("typeid", record.getTypeid());
		parameter.put("recharge_count",record.getRecharge_count());
		parameter.put("recharge_amount", record.getRecharge_amount());
		parameter.put("withdrawals_count", record.getWithdrawals_count());
		parameter.put("withdrawals_amount", record.getWithdrawals_amount());
		parameter.put("game_count",record.getGame_count());
		parameter.put("bet_amount", record.getBet_amount());
		parameter.put("paid_amount", record.getPaid_amount());
		parameter.put("updatetime", record.getUpdatetime());
		if (1!=Dao.insert("reportdayInsert", parameter))
			return false;
		return true;
	}
}

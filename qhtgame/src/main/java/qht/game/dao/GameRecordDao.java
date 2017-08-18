/*
 * 游戏记录接口
 * qht_gamerecord
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.GameRecord;
import qht.game.node.GameRecordPlayer;
import qht.game.node.GameReportNode;
import qht.game.node.ReportBetPaidByDayNode;

public class GameRecordDao {
	public boolean insert(GameRecord record) {
		if (1!=Dao.insert("gamerecordInsert",record.getParameter()))
			return false;
		return true;
	}

	public GameRecordPlayer selectcountbet(String period) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("period", period);
		return (GameRecordPlayer) Dao.selectOne("playerbetSelectOne", parameter);
	}
	public GameRecordPlayer selectcountpaid(String period) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("period", period);
		return (GameRecordPlayer) Dao.selectOne("playerpaidSelectOne", parameter);
	}
	
	@SuppressWarnings("unchecked")
	public List<GameRecord> selectList(Map<String, Object> parameter) {
		return (List<GameRecord>) Dao.selectList("gamerecordSelectList", parameter);
	}

	@SuppressWarnings("unchecked")
	public List<GameReportNode> sumSelectList(long beginTime, long endTime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("begintime", beginTime);
		parameter.put("endtime", endTime);
		return (List<GameReportNode>) Dao.selectList("gamereportSelectList", parameter);
	}

	public ReportBetPaidByDayNode countByTime(long begintime, long endtime) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("begintime", begintime);
		parameter.put("endtime", endtime);
		return (ReportBetPaidByDayNode) Dao.selectOne("gamerecordCountByTime", parameter);
	}

}

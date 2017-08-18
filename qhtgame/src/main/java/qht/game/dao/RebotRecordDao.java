/*
 * pk10机器人投注记录
 * qht_robot_record
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.RebotRecordNode;

public class RebotRecordDao {

	@SuppressWarnings("unchecked")
	public List<RebotRecordNode> selectList(String period, String nickname, long begintime, long endtime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("period", period);
		parameter.put("nickname", nickname);
		parameter.put("begintime", begintime);
		parameter.put("endtime", endtime);
		return (List<RebotRecordNode>) Dao.selectList("rebotrecordSelectList", parameter);
	}

	public boolean insert(RebotRecordNode record) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", record.getUsername());
		parameter.put("nickname", record.getNickname());
		parameter.put("period", record.getPeriod());
		parameter.put("text", record.getText());
		parameter.put("status", record.getStatus());
		parameter.put("updatetime", record.getUpdatetime());
		
		if (1!=Dao.insert("rebotrecordInsert", parameter))
			return false;
		return true;
	}

}

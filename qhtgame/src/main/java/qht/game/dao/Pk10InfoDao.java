/*
 * pk10明细接口
 * qht_pk10_info
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sysconst.Code;
import com.util.Dao;
import com.util.DateUtil;

import qht.game.node.Pk10InfoNode;
import qht.game.node.Pk10InfoStatitisNode;

public class Pk10InfoDao {

	public boolean insert(Pk10InfoNode node) {

		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("odd", node.getOdd());
		parameter.put("period", node.getPeriod());
		parameter.put("appcode", node.getAppcode());
		parameter.put("username", node.getUsername());
		parameter.put("runway", node.getRunway());
		parameter.put("bettype", node.getBettype());
		parameter.put("betamount", node.getBetamount());
		parameter.put("paidamount", node.getPaidamount());
		parameter.put("bettime", node.getBettime());
		parameter.put("paidtime", node.getPaidtime());
		parameter.put("status", node.getStatus());
		parameter.put("updatetime", node.getUpdatetime());
		
		if (1!=Dao.insert("pk10infoInsertOne", parameter))
			return false;
		return true;
	}

	public boolean updateStatus(String odd, int status) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("odd", odd);
		parameter.put("status", status);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		if (1!=Dao.update("pk10infoUpdateStatus",parameter))
			return false;
		return true;
	}

	public boolean settle(Pk10InfoNode node) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("odd", node.getOdd());
		parameter.put("paidamount", node.getPaidamount());
		parameter.put("paidtime", node.getPaidtime());
		parameter.put("status", node.getStatus());
		parameter.put("updatetime", node.getUpdatetime());
		if (1!=Dao.update("pk10infoUpdateSettle",parameter))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Pk10InfoNode> selectCurPeriod(String username, String period) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		parameter.put("period", period);
		
		return (List<Pk10InfoNode>) Dao.selectList("pk10infoSelectCurPeriod", parameter);
		
	}

	@SuppressWarnings("unchecked")
	public List<Pk10InfoNode> selectByDate(String username, int status, long begintime, long endtime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		parameter.put("status", status);
		parameter.put("begintime", begintime);
		parameter.put("endtime", endtime);
		
		return (List<Pk10InfoNode>) Dao.selectList("pk10infoSelectByDate", parameter);
	}

	@SuppressWarnings("unchecked")
	public List<Pk10InfoNode> selectByPeriod(String username, int status, String period) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		parameter.put("status", status);
		parameter.put("period", period);
		
		return (List<Pk10InfoNode>) Dao.selectList("pk10infoSelectByPeriod", parameter);
	}

	public Pk10InfoStatitisNode statitisByDate(String username, int status, long begintime, long endtime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		parameter.put("status", status);
		parameter.put("begintime", begintime);
		parameter.put("endtime", endtime);
		
		return (Pk10InfoStatitisNode) Dao.selectOne("pk10infoStatitisByDate", parameter);
	}

	@SuppressWarnings("unchecked")
	public List<Pk10InfoNode> getNotSettle() {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("status1", Code.PK10_BET);
		parameter.put("status2", Code.PK10_HANDICAP);
		
		return (List<Pk10InfoNode>) Dao.selectList("pk10infoNotSettle", parameter);
	}
}

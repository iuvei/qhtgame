package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sysconst.Code;
import com.util.Dao;
import com.util.DateUtil;

import qht.game.node.Pk10HandicapBetNode;

public class Pk10HandicapDao {
	@SuppressWarnings("unchecked")
	public List<Pk10HandicapBetNode> selectlist(int type, String period) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("type", type);
		parameter.put("period", period);
		parameter.put("status", Code.PK10_HANDICAP_BET);
		
		return (List<Pk10HandicapBetNode>) Dao.selectList("pk10handicapSelectList2", parameter);
	}

	public boolean insertOne(Pk10HandicapBetNode betInfo) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("odd", betInfo.getOdd());
		parameter.put("period", betInfo.getPeriod());
		parameter.put("runway", betInfo.getRunway());
		parameter.put("bettype", betInfo.getBettype());
		parameter.put("betamount", betInfo.getBetamount());
		parameter.put("status", betInfo.getStatus());
		parameter.put("eventuate", betInfo.getEventuate());
		parameter.put("updatetime", betInfo.getUpdatetime());
		parameter.put("createtime", betInfo.getCreatetime());
		if (1!=Dao.insert("pk10handicapInsertOne",parameter))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Pk10HandicapBetNode> statusSelectlist(String period, int status) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("period", period);
		parameter.put("status", status);
		
		return (List<Pk10HandicapBetNode>) Dao.selectList("pk10handicapSelectList", parameter);
	}

	public boolean updateStatus(String odd, int status, String eventuate) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("odd", odd);
		parameter.put("status", status);
		parameter.put("eventuate", eventuate);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		if (1!=Dao.update("pk10handicapupdateStatus",parameter))
			return false;
		return true;
	}
	
}

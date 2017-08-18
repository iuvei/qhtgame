package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;
import com.util.DateUtil;

import qht.game.node.Pk10HandicapBetNode;

public class Pk10HandicapDao {

	public boolean insertOne(Pk10HandicapBetNode betInfo) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("odd", betInfo.getOdd());
		parameter.put("period", betInfo.getPeriod());
		parameter.put("context", betInfo.getContext());
		parameter.put("eventuate", betInfo.getEventuate());
		parameter.put("status", betInfo.getStatus());
		parameter.put("updatetime", betInfo.getUpdatetime());
		parameter.put("createtime", betInfo.getCreatetime());
		if (1!=Dao.insert("pk10handicapInsertOne",parameter))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Pk10HandicapBetNode> statusSelectlist(int status) {
		Map<String,Object> parameter = new HashMap<String,Object>();
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

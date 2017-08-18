/*
 * 返水信息接口
 * qht_returnwater
 */
package qht.game.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.ReturnwaterNode;

public class ReturnwaterDao {

	public boolean insert(ReturnwaterNode record) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("date", record.getDate());
		parameter.put("appcode", record.getAppcode());
		parameter.put("username", record.getUsername());
		parameter.put("typeid",record.getTypeid());
		parameter.put("water_amount", record.getWater_amount());
		parameter.put("profit_amount", record.getProfit_amount());
		parameter.put("up_amount", record.getUp_amount());
		parameter.put("down_amount",record.getDown_amount());
		parameter.put("status", record.getStatus());
		parameter.put("return_amount", record.getReturn_amount());
		parameter.put("updatetime", record.getUpdatetime());
		parameter.put("createtime", record.getCreatetime());
		if (1!=Dao.insert("returnwaterInsert", parameter))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<ReturnwaterNode> selectlist(int date, int status) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("date", date);
		parameter.put("status", status);
		return (List<ReturnwaterNode>) Dao.selectList("returnwaterSelectlist", parameter);
	}
	
	@SuppressWarnings("unchecked")
	public List<ReturnwaterNode> selectlist2(int date, int status) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("date", date);
		parameter.put("status", status);
		return (List<ReturnwaterNode>) Dao.selectList("returnwaterSelectlist2", parameter);
	}

	public boolean updateStatus(long id, int status, BigDecimal return_amount, long updatetime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("status", status);
		parameter.put("return_amount", return_amount);
		parameter.put("updatetime", updatetime);
		if (1!=Dao.update("returnwaterUpdate",parameter))
			return false;
		return true;
	}
}
